# Home Feature Design

Date: 2026-08-22
Status: Approved

## Summary

New `home` feature: single entry screen with a bottom navigation bar
(Home / Search / Profile). The Home tab shows three horizontal movie
sections — Now Playing, Latest, Upcoming — fetched from TMDB. Search and
Profile tabs are placeholder screens only (out of scope for this task).

## Scope decisions

- Search and Profile tabs: placeholder composables, no state/network. Full
  implementation is a separate future spec.
- "Latest" section is sourced from TMDB `/movie/top_rated` (TMDB's actual
  `/movie/latest` endpoint returns a single most-recently-added movie, not a
  list, so it's unsuitable for a section).
- Bottom nav tab switching is local UI state inside the home feature, not a
  Navigation 3 back-stack entry per tab — appropriate because two of the
  three tabs are inert placeholders and the tab switch never needs to be
  deep-linked or back-navigated independently.
- Each of the three movie sections has independent loading/success/error
  state; a failure in one section does not affect the others.
- The existing unused `MovieRepository` interface in
  `feature/onboarding/domain` is left untouched (out of scope) — home gets
  its own repository.

## Module layout

New Gradle modules, following the existing 4-module feature shape
(`api` / `domain` / `data` / `presentation`), registered in
`settings.gradle.kts`:

- `feature:home:api`
- `feature:home:domain`
- `feature:home:data`
- `feature:home:presentation`

## Design system additions (`core:designsystem`)

- `JoyvieBottomNavBar(items: List<JoyvieBottomNavItem>, selected: Int, onSelect: (Int) -> Unit)`
  — atom, generic bottom nav bar (not home-specific, reusable).
- `JoyvieMovieSection(title: String, state: SectionUiState, onRetry: () -> Unit)`
  — molecule, horizontal `LazyRow` of `JoyviePosterCard` (existing atom) with
  loading/error/empty states and a retry action.

## Domain (`feature:home:domain`)

```kotlin
interface HomeRepository {
    suspend fun getNowPlayingMovies(): Result<List<Movie>>
    suspend fun getTopRatedMovies(): Result<List<Movie>>
    suspend fun getUpcomingMovies(): Result<List<Movie>>
}
```

Use cases (thin delegation, mirroring `GetOnboardingPagesUseCase`):
`GetNowPlayingMoviesUseCase`, `GetTopRatedMoviesUseCase`,
`GetUpcomingMoviesUseCase`.

No new model types — reuses `core:model`'s `Movie`.

## Data (`feature:home:data`)

`HomeRepositoryImpl(private val httpClient: HttpClient) : HomeRepository`
(`HttpClient` is already Koin-provided as `@Single` in
`core/network/di/NetworkModule.kt`).

- `GET 3/movie/now_playing`
- `GET 3/movie/top_rated`
- `GET 3/movie/upcoming`

Each call: `safeApiCall { httpClient.get(path).body<MovieResponseDto>().results.map { it.toDomain() } }`.
New mapper `MovieDto.toDomain(): Movie` (reuses existing `MovieDto` /
`MovieResponseDto` in `core:network`).

## Presentation (`feature:home:presentation`)

```kotlin
enum class HomeTab { Home, Search, Profile }

sealed interface SectionUiState {
    data object Loading : SectionUiState
    data class Success(val movies: List<Movie>) : SectionUiState
    data class Error(val message: String) : SectionUiState
}

data class HomeState(
    val selectedTab: HomeTab = HomeTab.Home,
    val nowPlaying: SectionUiState = SectionUiState.Loading,
    val latest: SectionUiState = SectionUiState.Loading,
    val upcoming: SectionUiState = SectionUiState.Loading,
) : UiState

sealed interface HomeEvent : UiEvent {
    data object LoadHomeMovies : HomeEvent
    data class SelectTab(val tab: HomeTab) : HomeEvent
    data object RetryNowPlaying : HomeEvent
    data object RetryLatest : HomeEvent
    data object RetryUpcoming : HomeEvent
}
```

`HomeStateMachine` (`BaseStateMachine<HomeState, HomeEvent, Nothing>`, no
effects needed — tab switching and retries are handled entirely in-state):

- `init` fires `LoadHomeMovies`.
- `LoadHomeMovies` launches three independent coroutines in
  `viewModelScope`, one per use case; each updates only its own section
  field on completion (success or failure), never touching the other two.
- `SelectTab(tab)` is a pure `setState { copy(selectedTab = tab) }` — does
  not trigger any reload.
- `RetryNowPlaying` / `RetryLatest` / `RetryUpcoming` re-invoke only that
  section's use case.

`HomeScreen` composable: `Scaffold` with `JoyvieBottomNavBar` bound to
`selectedTab`; when tab is `Home`, renders three `JoyvieMovieSection`s bound
to `nowPlaying` / `latest` / `upcoming`; `Search` and `Profile` tabs render
placeholder composables (e.g. centered "Coming soon" text) with no state or
network wiring.

## Navigation

`feature:home:api` declares `@Serializable data object HomeRoute : NavKey`.
`feature:home:presentation` declares `homeEntries(backStack)` registering
`entry<HomeRoute> { HomeScreen(...) }`. `composeApp/AppNavigation.kt` adds
`homeEntries(backStack)` to its `entryProvider`. `OnboardingStateMachine`'s
existing `NavigateToHome` effect (currently unhandled by any route) now
resolves to `HomeRoute`.

## Error handling

Each use case call is wrapped by the repository's `safeApiCall`, returning
`Result<List<Movie>>`. On failure, the state machine sets that section's
state to `SectionUiState.Error(message)`; `JoyvieMovieSection` renders a
retry affordance that fires the section-specific retry event. Other
sections are unaffected.

## Testing plan (TDD, `commonTest`, mirrors existing onboarding test style)

1. **`HomeRepositoryImplTest`** — per endpoint: successful response maps
   `MovieResponseDto.results` to `List<Movie>` correctly; HTTP/network
   failure surfaces as `Result.failure`.
2. **`GetNowPlayingMoviesUseCaseTest`**, **`GetTopRatedMoviesUseCaseTest`**,
   **`GetUpcomingMoviesUseCaseTest`** — each delegates to the matching
   repository method and propagates success/failure unchanged (pattern:
   `GetOnboardingPagesUseCaseTest`).
3. **`HomeStateMachineTest`**:
   - initial state has all three sections `Loading`.
   - `init` triggers all three use cases; each section transitions to
     `Success` independently as its use case completes.
   - one section failing sets only that section to `Error`; the other two
     remain `Success`/unaffected (proves independence).
   - `SelectTab` updates `selectedTab` only, does not re-trigger any use
     case.
   - each retry event re-invokes only its own section's use case.

Compose UI (`JoyvieBottomNavBar`, `JoyvieMovieSection`, `HomeScreen`) is not
unit tested — no Compose UI test precedent exists in this codebase (auth
and onboarding features are unit-tested at data/domain/state-machine level
only, UI verified manually via previews). Same approach here.
