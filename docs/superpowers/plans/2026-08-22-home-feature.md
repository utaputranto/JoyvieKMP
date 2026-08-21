# Home Feature Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a `home` feature with a bottom-navigation Home screen (Home / Search / Profile tabs) whose Home tab shows three independently-loading TMDB movie sections (Now Playing, Latest = Top Rated, Upcoming).

**Architecture:** New 4-module Gradle feature (`api`/`domain`/`data`/`presentation`) following the existing `onboarding`/`auth` shape. `HomeRepository` hits TMDB via the already-provided Koin `HttpClient`. `HomeStateMachine` (MVI, `BaseStateMachine`) tracks per-section loading/success/error state plus a local `selectedTab` — no back-stack entry per tab. Two new reusable `core:designsystem` components (`JoyvieBottomNavBar`, `JoyvieMovieSection`) render the UI.

**Tech Stack:** Kotlin Multiplatform, Compose Multiplatform, Koin (`@Single`/`@Factory`/`@KoinViewModel` + `@ComponentScan`), Ktor client, kotlinx.serialization, Kotest (`FunSpec`) + Turbine for tests, Navigation 3.

## Global Constraints

- Follow the existing 4-module feature shape exactly (`api`, `domain`, `data`, `presentation`), registered in `settings.gradle.kts` (spec: Module layout).
- "Latest" section sources from TMDB `/movie/top_rated`, not `/movie/latest` (spec: Scope decisions).
- Bottom-nav tab switching is local Compose/state-machine state, not a Nav3 back-stack entry (spec: Scope decisions).
- Each of the three movie sections (`nowPlaying`, `latest`, `upcoming`) has fully independent loading/success/error state — one section's failure must never affect another (spec: Scope decisions, Presentation).
- Search and Profile tabs are placeholder composables only — no state, no network, no tests (spec: Scope decisions).
- Reuse `core:model`'s `Movie` and `core:network`'s `MovieDto`/`MovieResponseDto` — no new model types (spec: Domain, Data).
- Leave the existing unused `MovieRepository` in `feature/onboarding/domain` untouched (spec: Scope decisions).
- Compose UI (`JoyvieBottomNavBar`, `JoyvieMovieSection`, `HomeScreen`, placeholders) is not unit tested — no Compose UI test precedent exists in this codebase; verify via `spotlessApply`/build success only, matching `auth`/`onboarding` (spec: Testing plan).
- All Kotlin must pass `./gradlew spotlessCheck` (ktlint via Spotless) before a task is considered done.

---

### Task 1: Gradle module scaffolding

**Files:**
- Modify: `settings.gradle.kts`
- Modify: `gradle/libs.versions.toml`
- Modify: `core/test/build.gradle.kts`
- Create: `feature/home/api/build.gradle.kts`
- Create: `feature/home/domain/build.gradle.kts`
- Create: `feature/home/data/build.gradle.kts`
- Create: `feature/home/presentation/build.gradle.kts`

**Interfaces:**
- Produces: 4 buildable (empty) Gradle modules `:feature:home:api`, `:feature:home:domain`, `:feature:home:data`, `:feature:home:presentation`; `libs.ktor.client.mock` version catalog entry; `io.ktor:ktor-client-mock` available to every module's `commonTest` (via `core:test`).

This is build configuration, not application logic — no test cycle applies. Verified by a build command instead.

- [ ] **Step 1: Register the four new modules in `settings.gradle.kts`**

Append after the existing `:feature:onboarding:*` includes (`settings.gradle.kts:50`):

```kotlin
include(":feature:home:api")
include(":feature:home:domain")
include(":feature:home:data")
include(":feature:home:presentation")
```

- [ ] **Step 2: Add the `ktor-client-mock` version catalog entry**

In `gradle/libs.versions.toml`, in the `# Ktor Client Dependencies` block (after the `ktor-serialization-kotlinx-json` line), add:

```toml
ktor-client-mock = { module = "io.ktor:ktor-client-mock", version.ref = "ktor" }
```

- [ ] **Step 3: Expose `ktor-client-mock` to every module's `commonTest`**

In `core/test/build.gradle.kts`, add to the `commonMain.dependencies` block (alongside the existing `api(libs.turbine)` line):

```kotlin
            api(libs.ktor.client.mock)
```

- [ ] **Step 4: Create `feature/home/api/build.gradle.kts`**

```kotlin
plugins {
    alias(libs.plugins.joyvie.feature.api)
}
```

- [ ] **Step 5: Create `feature/home/domain/build.gradle.kts`**

```kotlin
plugins {
    alias(libs.plugins.joyvie.feature.domain)
    alias(libs.plugins.koin.compiler)
}

// Use-case @Factory deps are repository interfaces implemented in the data layer
// (dependency inversion — data depends on domain, not the reverse), so their providers
// are never visible to a static check inside domain. Resolved at app-level aggregation.
koinCompiler {
    compileSafety = false
}
```

- [ ] **Step 6: Create `feature/home/data/build.gradle.kts`**

```kotlin
plugins {
    alias(libs.plugins.joyvie.feature.data)
    alias(libs.plugins.koin.compiler)
}

// Repo depends on the HttpClient provider in core:network; the plugin's static check
// doesn't resolve it across module boundaries. Verified at app-level aggregation.
koinCompiler {
    compileSafety = false
}
```

- [ ] **Step 7: Create `feature/home/presentation/build.gradle.kts`**

```kotlin
plugins {
    alias(libs.plugins.joyvie.feature.presentation)
    alias(libs.plugins.koin.compiler)
}

// ViewModel @KoinViewModel deps (use cases) are provided by the domain module; the
// plugin's static check doesn't resolve them across the boundary. Verified at aggregation.
koinCompiler {
    compileSafety = false
}
```

- [ ] **Step 8: Verify the empty modules build**

Run: `./gradlew :feature:home:api:build :feature:home:domain:build :feature:home:data:build :feature:home:presentation:build`
Expected: BUILD SUCCESSFUL (each module compiles with no source files yet).

- [ ] **Step 9: Commit**

```bash
git add settings.gradle.kts gradle/libs.versions.toml core/test/build.gradle.kts feature/home
git commit -m "chore: scaffold feature:home Gradle modules"
```

---

### Task 2: Home domain layer — repository contract + use cases

**Files:**
- Create: `feature/home/domain/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/domain/repository/HomeRepository.kt`
- Create: `feature/home/domain/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/domain/di/HomeDomainModule.kt`
- Create: `feature/home/domain/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/domain/fakes/FakeHomeRepository.kt`
- Create: `feature/home/domain/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/domain/usecase/GetNowPlayingMoviesUseCase.kt`
- Create: `feature/home/domain/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/domain/usecase/GetNowPlayingMoviesUseCaseTest.kt`
- Create: `feature/home/domain/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/domain/usecase/GetTopRatedMoviesUseCase.kt`
- Create: `feature/home/domain/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/domain/usecase/GetTopRatedMoviesUseCaseTest.kt`
- Create: `feature/home/domain/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/domain/usecase/GetUpcomingMoviesUseCase.kt`
- Create: `feature/home/domain/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/domain/usecase/GetUpcomingMoviesUseCaseTest.kt`

**Interfaces:**
- Consumes: `com.utaputranto.joyviekmp.core.model.Movie(id: Int, title: String, overview: String, posterPath: String?, releaseDate: String, voteAverage: Double)` (already exists).
- Produces: `HomeRepository` interface with `getNowPlayingMovies()`, `getTopRatedMovies()`, `getUpcomingMovies()` — each `suspend fun ...(): Result<List<Movie>>`. `GetNowPlayingMoviesUseCase`, `GetTopRatedMoviesUseCase`, `GetUpcomingMoviesUseCase` — each a `@Factory class` with `private val repository: HomeRepository` and `suspend operator fun invoke(): Result<List<Movie>>`. `FakeHomeRepository` test double in `feature.home.domain.fakes` — later reused as the *pattern* (not the same class — presentation gets its own copy in Task 4) for `HomeStateMachineTest`.

- [ ] **Step 1: Create the `HomeRepository` interface**

This is a type declaration needed for the fake/use cases below to compile — mirrors how `OnboardingRepository` was introduced in this codebase (interfaces aren't independently unit tested; the use cases wrapping them are).

`feature/home/domain/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/domain/repository/HomeRepository.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.domain.repository

import com.utaputranto.joyviekmp.core.model.Movie

interface HomeRepository {
    suspend fun getNowPlayingMovies(): Result<List<Movie>>

    suspend fun getTopRatedMovies(): Result<List<Movie>>

    suspend fun getUpcomingMovies(): Result<List<Movie>>
}
```

- [ ] **Step 2: Create the domain Koin module aggregator**

`feature/home/domain/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/domain/di/HomeDomainModule.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.domain.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

/** Aggregates annotated home-domain definitions (@Factory use cases) into a module hint. */
@Module
@ComponentScan("com.utaputranto.joyviekmp.feature.home.domain")
class HomeDomainModule
```

- [ ] **Step 3: Create `FakeHomeRepository`**

`feature/home/domain/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/domain/fakes/FakeHomeRepository.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.domain.fakes

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.repository.HomeRepository

class FakeHomeRepository : HomeRepository {
    private var nowPlayingResult: Result<List<Movie>> = Result.success(emptyList())
    private var topRatedResult: Result<List<Movie>> = Result.success(emptyList())
    private var upcomingResult: Result<List<Movie>> = Result.success(emptyList())

    fun setNowPlayingResult(result: Result<List<Movie>>) {
        nowPlayingResult = result
    }

    fun setTopRatedResult(result: Result<List<Movie>>) {
        topRatedResult = result
    }

    fun setUpcomingResult(result: Result<List<Movie>>) {
        upcomingResult = result
    }

    override suspend fun getNowPlayingMovies(): Result<List<Movie>> = nowPlayingResult

    override suspend fun getTopRatedMovies(): Result<List<Movie>> = topRatedResult

    override suspend fun getUpcomingMovies(): Result<List<Movie>> = upcomingResult
}
```

- [ ] **Step 4: Write the failing test for `GetNowPlayingMoviesUseCase`**

`feature/home/domain/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/domain/usecase/GetNowPlayingMoviesUseCaseTest.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.domain.usecase

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.fakes.FakeHomeRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GetNowPlayingMoviesUseCaseTest : FunSpec({

    lateinit var fakeRepository: FakeHomeRepository
    lateinit var useCase: GetNowPlayingMoviesUseCase

    beforeTest {
        fakeRepository = FakeHomeRepository()
        useCase = GetNowPlayingMoviesUseCase(fakeRepository)
    }

    test("invoke should return now playing movies from repository") {
        val expectedMovies =
            listOf(
                Movie(
                    id = 1,
                    title = "Now Playing Movie",
                    overview = "Overview",
                    posterPath = "/poster.jpg",
                    releaseDate = "2026-01-01",
                    voteAverage = 7.5,
                ),
            )
        fakeRepository.setNowPlayingResult(Result.success(expectedMovies))

        val actual = useCase()

        actual shouldBe Result.success(expectedMovies)
    }

    test("invoke should propagate failure from repository") {
        val expectedError = RuntimeException("network error")
        fakeRepository.setNowPlayingResult(Result.failure(expectedError))

        val actual = useCase()

        actual shouldBe Result.failure<List<Movie>>(expectedError)
    }
})
```

- [ ] **Step 5: Run the test and verify it fails**

Run: `./gradlew :feature:home:domain:test --tests "*.GetNowPlayingMoviesUseCaseTest"`
Expected: FAIL — `GetNowPlayingMoviesUseCase` is unresolved (class doesn't exist yet).

- [ ] **Step 6: Implement `GetNowPlayingMoviesUseCase`**

`feature/home/domain/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/domain/usecase/GetNowPlayingMoviesUseCase.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.domain.usecase

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.repository.HomeRepository
import org.koin.core.annotation.Factory

@Factory
class GetNowPlayingMoviesUseCase(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(): Result<List<Movie>> = repository.getNowPlayingMovies()
}
```

- [ ] **Step 7: Run the test and verify it passes**

Run: `./gradlew :feature:home:domain:test --tests "*.GetNowPlayingMoviesUseCaseTest"`
Expected: PASS (2 tests green).

- [ ] **Step 8: Write the failing test for `GetTopRatedMoviesUseCase`**

`feature/home/domain/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/domain/usecase/GetTopRatedMoviesUseCaseTest.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.domain.usecase

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.fakes.FakeHomeRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GetTopRatedMoviesUseCaseTest : FunSpec({

    lateinit var fakeRepository: FakeHomeRepository
    lateinit var useCase: GetTopRatedMoviesUseCase

    beforeTest {
        fakeRepository = FakeHomeRepository()
        useCase = GetTopRatedMoviesUseCase(fakeRepository)
    }

    test("invoke should return top rated movies from repository") {
        val expectedMovies =
            listOf(
                Movie(
                    id = 2,
                    title = "Top Rated Movie",
                    overview = "Overview",
                    posterPath = "/poster2.jpg",
                    releaseDate = "2025-06-01",
                    voteAverage = 8.9,
                ),
            )
        fakeRepository.setTopRatedResult(Result.success(expectedMovies))

        val actual = useCase()

        actual shouldBe Result.success(expectedMovies)
    }

    test("invoke should propagate failure from repository") {
        val expectedError = RuntimeException("network error")
        fakeRepository.setTopRatedResult(Result.failure(expectedError))

        val actual = useCase()

        actual shouldBe Result.failure<List<Movie>>(expectedError)
    }
})
```

- [ ] **Step 9: Run the test and verify it fails**

Run: `./gradlew :feature:home:domain:test --tests "*.GetTopRatedMoviesUseCaseTest"`
Expected: FAIL — `GetTopRatedMoviesUseCase` is unresolved.

- [ ] **Step 10: Implement `GetTopRatedMoviesUseCase`**

`feature/home/domain/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/domain/usecase/GetTopRatedMoviesUseCase.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.domain.usecase

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.repository.HomeRepository
import org.koin.core.annotation.Factory

@Factory
class GetTopRatedMoviesUseCase(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(): Result<List<Movie>> = repository.getTopRatedMovies()
}
```

- [ ] **Step 11: Run the test and verify it passes**

Run: `./gradlew :feature:home:domain:test --tests "*.GetTopRatedMoviesUseCaseTest"`
Expected: PASS (2 tests green).

- [ ] **Step 12: Write the failing test for `GetUpcomingMoviesUseCase`**

`feature/home/domain/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/domain/usecase/GetUpcomingMoviesUseCaseTest.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.domain.usecase

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.fakes.FakeHomeRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GetUpcomingMoviesUseCaseTest : FunSpec({

    lateinit var fakeRepository: FakeHomeRepository
    lateinit var useCase: GetUpcomingMoviesUseCase

    beforeTest {
        fakeRepository = FakeHomeRepository()
        useCase = GetUpcomingMoviesUseCase(fakeRepository)
    }

    test("invoke should return upcoming movies from repository") {
        val expectedMovies =
            listOf(
                Movie(
                    id = 3,
                    title = "Upcoming Movie",
                    overview = "Overview",
                    posterPath = "/poster3.jpg",
                    releaseDate = "2026-12-01",
                    voteAverage = 0.0,
                ),
            )
        fakeRepository.setUpcomingResult(Result.success(expectedMovies))

        val actual = useCase()

        actual shouldBe Result.success(expectedMovies)
    }

    test("invoke should propagate failure from repository") {
        val expectedError = RuntimeException("network error")
        fakeRepository.setUpcomingResult(Result.failure(expectedError))

        val actual = useCase()

        actual shouldBe Result.failure<List<Movie>>(expectedError)
    }
})
```

- [ ] **Step 13: Run the test and verify it fails**

Run: `./gradlew :feature:home:domain:test --tests "*.GetUpcomingMoviesUseCaseTest"`
Expected: FAIL — `GetUpcomingMoviesUseCase` is unresolved.

- [ ] **Step 14: Implement `GetUpcomingMoviesUseCase`**

`feature/home/domain/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/domain/usecase/GetUpcomingMoviesUseCase.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.domain.usecase

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.repository.HomeRepository
import org.koin.core.annotation.Factory

@Factory
class GetUpcomingMoviesUseCase(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(): Result<List<Movie>> = repository.getUpcomingMovies()
}
```

- [ ] **Step 15: Run the full domain test suite and verify all pass**

Run: `./gradlew :feature:home:domain:test`
Expected: PASS (6 tests green: 2 per use case).

- [ ] **Step 16: Commit**

```bash
git add feature/home/domain
git commit -m "feat(home): add HomeRepository contract and movie use cases"
```

---

### Task 3: Home data layer — TMDB repository implementation

**Files:**
- Create: `feature/home/data/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/data/mapper/MovieMapper.kt`
- Create: `feature/home/data/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/data/repository/HomeRepositoryImpl.kt`
- Create: `feature/home/data/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/data/repository/HomeRepositoryImplTest.kt`
- Create: `feature/home/data/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/data/di/HomeDataModule.kt`

**Interfaces:**
- Consumes: `HomeRepository` (Task 2); `com.utaputranto.joyviekmp.core.network.model.MovieDto(id: Int, title: String, overview: String, posterPath: String?, releaseDate: String, voteAverage: Double)` and `MovieResponseDto(page: Int, results: List<MovieDto>, totalPages: Int, totalResults: Int)` (already exist); `com.utaputranto.joyviekmp.core.network.safeApiCall` (already exists); `io.ktor.client.HttpClient` (Koin `@Single` from `core/network/di/NetworkModule.kt`).
- Produces: `fun MovieDto.toDomain(): Movie`; `HomeRepositoryImpl(private val httpClient: HttpClient) : HomeRepository`, `@Single(binds = [HomeRepository::class])`.

- [ ] **Step 1: Write the failing test for `getNowPlayingMovies` success mapping**

`feature/home/data/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/data/repository/HomeRepositoryImplTest.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.data.repository

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.core.network.model.MovieDto
import com.utaputranto.joyviekmp.core.network.model.MovieResponseDto
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HomeRepositoryImplTest : FunSpec({

    val testJson = Json { ignoreUnknownKeys = true }

    val sampleDto =
        MovieDto(
            id = 42,
            title = "Sample Movie",
            overview = "A sample overview",
            posterPath = "/sample.jpg",
            releaseDate = "2026-03-01",
            voteAverage = 7.2,
        )
    val sampleMovie =
        Movie(
            id = 42,
            title = "Sample Movie",
            overview = "A sample overview",
            posterPath = "/sample.jpg",
            releaseDate = "2026-03-01",
            voteAverage = 7.2,
        )

    fun clientReturning(
        dto: MovieResponseDto,
        expectedPath: String,
    ): HttpClient {
        val engine =
            MockEngine { request ->
                request.url.encodedPath shouldBe "/$expectedPath"
                respond(
                    content = testJson.encodeToString(MovieResponseDto.serializer(), dto),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
        return HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) { json(testJson) }
        }
    }

    fun clientReturningError(statusCode: HttpStatusCode): HttpClient {
        val engine = MockEngine { respondError(statusCode) }
        return HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) { json(testJson) }
        }
    }

    test("getNowPlayingMovies should map response to domain movies") {
        val responseDto = MovieResponseDto(page = 1, results = listOf(sampleDto), totalPages = 1, totalResults = 1)
        val repository = HomeRepositoryImpl(clientReturning(responseDto, "3/movie/now_playing"))

        val actual = repository.getNowPlayingMovies()

        actual shouldBe Result.success(listOf(sampleMovie))
    }

    test("getNowPlayingMovies should return failure on HTTP error") {
        val repository = HomeRepositoryImpl(clientReturningError(HttpStatusCode.InternalServerError))

        val actual = repository.getNowPlayingMovies()

        actual.isFailure shouldBe true
    }
})
```

- [ ] **Step 2: Run the test and verify it fails**

Run: `./gradlew :feature:home:data:test --tests "*.HomeRepositoryImplTest"`
Expected: FAIL — `HomeRepositoryImpl` is unresolved (class doesn't exist yet).

- [ ] **Step 3: Implement the mapper and `HomeRepositoryImpl` for `getNowPlayingMovies`**

`feature/home/data/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/data/mapper/MovieMapper.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.data.mapper

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.core.network.model.MovieDto

fun MovieDto.toDomain(): Movie =
    Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
    )
```

`feature/home/data/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/data/repository/HomeRepositoryImpl.kt` (stub the other two endpoints for now — implemented in later steps):

```kotlin
package com.utaputranto.joyviekmp.feature.home.data.repository

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.core.network.model.MovieResponseDto
import com.utaputranto.joyviekmp.core.network.safeApiCall
import com.utaputranto.joyviekmp.feature.home.data.mapper.toDomain
import com.utaputranto.joyviekmp.feature.home.domain.repository.HomeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Single

@Single(binds = [HomeRepository::class])
class HomeRepositoryImpl(
    private val httpClient: HttpClient,
) : HomeRepository {
    override suspend fun getNowPlayingMovies(): Result<List<Movie>> = fetchMovies(NOW_PLAYING_PATH)

    override suspend fun getTopRatedMovies(): Result<List<Movie>> = TODO("implemented in Task 3 Step 5")

    override suspend fun getUpcomingMovies(): Result<List<Movie>> = TODO("implemented in Task 3 Step 7")

    private suspend fun fetchMovies(path: String): Result<List<Movie>> =
        safeApiCall {
            httpClient.get(path).body<MovieResponseDto>().results.map { it.toDomain() }
        }

    private companion object {
        const val NOW_PLAYING_PATH = "3/movie/now_playing"
    }
}
```

- [ ] **Step 4: Run the test and verify it passes**

Run: `./gradlew :feature:home:data:test --tests "*.HomeRepositoryImplTest"`
Expected: PASS (2 tests green).

- [ ] **Step 5: Write the failing test for `getTopRatedMovies`**

Add to `HomeRepositoryImplTest.kt`, inside the same `FunSpec` block (after the two existing `test(...)` blocks):

```kotlin
    test("getTopRatedMovies should map response to domain movies") {
        val responseDto = MovieResponseDto(page = 1, results = listOf(sampleDto), totalPages = 1, totalResults = 1)
        val repository = HomeRepositoryImpl(clientReturning(responseDto, "3/movie/top_rated"))

        val actual = repository.getTopRatedMovies()

        actual shouldBe Result.success(listOf(sampleMovie))
    }
```

- [ ] **Step 6: Run the test and verify it fails**

Run: `./gradlew :feature:home:data:test --tests "*.HomeRepositoryImplTest"`
Expected: FAIL — `getTopRatedMovies should map response to domain movies` fails with `NotImplementedError` (`TODO`).

- [ ] **Step 7: Implement `getTopRatedMovies`**

In `HomeRepositoryImpl.kt`, replace:

```kotlin
    override suspend fun getTopRatedMovies(): Result<List<Movie>> = TODO("implemented in Task 3 Step 5")
```

with:

```kotlin
    override suspend fun getTopRatedMovies(): Result<List<Movie>> = fetchMovies(TOP_RATED_PATH)
```

and add `TOP_RATED_PATH` to the companion object:

```kotlin
    private companion object {
        const val NOW_PLAYING_PATH = "3/movie/now_playing"
        const val TOP_RATED_PATH = "3/movie/top_rated"
    }
```

- [ ] **Step 8: Run the test and verify it passes**

Run: `./gradlew :feature:home:data:test --tests "*.HomeRepositoryImplTest"`
Expected: PASS (3 tests green).

- [ ] **Step 9: Write the failing test for `getUpcomingMovies`**

Add to `HomeRepositoryImplTest.kt`:

```kotlin
    test("getUpcomingMovies should map response to domain movies") {
        val responseDto = MovieResponseDto(page = 1, results = listOf(sampleDto), totalPages = 1, totalResults = 1)
        val repository = HomeRepositoryImpl(clientReturning(responseDto, "3/movie/upcoming"))

        val actual = repository.getUpcomingMovies()

        actual shouldBe Result.success(listOf(sampleMovie))
    }
```

- [ ] **Step 10: Run the test and verify it fails**

Run: `./gradlew :feature:home:data:test --tests "*.HomeRepositoryImplTest"`
Expected: FAIL — `getUpcomingMovies should map response to domain movies` fails with `NotImplementedError`.

- [ ] **Step 11: Implement `getUpcomingMovies`**

In `HomeRepositoryImpl.kt`, replace:

```kotlin
    override suspend fun getUpcomingMovies(): Result<List<Movie>> = TODO("implemented in Task 3 Step 7")
```

with:

```kotlin
    override suspend fun getUpcomingMovies(): Result<List<Movie>> = fetchMovies(UPCOMING_PATH)
```

and add `UPCOMING_PATH` to the companion object:

```kotlin
    private companion object {
        const val NOW_PLAYING_PATH = "3/movie/now_playing"
        const val TOP_RATED_PATH = "3/movie/top_rated"
        const val UPCOMING_PATH = "3/movie/upcoming"
    }
```

- [ ] **Step 12: Run the full data test suite and verify all pass**

Run: `./gradlew :feature:home:data:test`
Expected: PASS (5 tests green).

- [ ] **Step 13: Create the data Koin module aggregator**

`feature/home/data/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/data/di/HomeDataModule.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.data.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

/** Aggregates annotated home-data definitions (@Single HomeRepositoryImpl) into a module hint. */
@Module
@ComponentScan("com.utaputranto.joyviekmp.feature.home.data")
class HomeDataModule
```

- [ ] **Step 14: Commit**

```bash
git add feature/home/data
git commit -m "feat(home): implement HomeRepository against TMDB"
```

---

### Task 4: Home presentation layer — state, events, state machine

**Files:**
- Create: `feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/HomeContract.kt`
- Create: `feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/HomeStateMachine.kt`
- Create: `feature/home/presentation/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/fakes/FakeHomeRepository.kt`
- Create: `feature/home/presentation/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/HomeStateMachineTest.kt`
- Create: `feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/di/HomePresentationModule.kt`

**Interfaces:**
- Consumes: `GetNowPlayingMoviesUseCase`, `GetTopRatedMoviesUseCase`, `GetUpcomingMoviesUseCase` (Task 2, each `suspend operator fun invoke(): Result<List<Movie>>`); `HomeRepository` (Task 2, to implement the presentation-side fake); `com.utaputranto.joyviekmp.core.mvi.BaseStateMachine`, `UiState`, `UiEvent`, `UiEffect`; `com.utaputranto.joyviekmp.core.test.rules.MainDispatcherRule`.
- Produces: `enum class HomeTab { Home, Search, Profile }`; `sealed interface SectionUiState { Loading, Success(movies: List<Movie>), Error(message: String) }`; `data class HomeState(selectedTab: HomeTab, nowPlaying: SectionUiState, latest: SectionUiState, upcoming: SectionUiState) : UiState`; `sealed interface HomeEvent : UiEvent` (grows across steps below); `sealed interface HomeEffect : UiEffect` (empty — no navigation effects needed for this feature); `class HomeStateMachine(getNowPlayingMovies, getTopRatedMovies, getUpcomingMovies) : BaseStateMachine<HomeState, HomeEvent, HomeEffect>`, `@KoinViewModel`.

- [ ] **Step 1: Write the failing test for `HomeState` defaults**

`feature/home/presentation/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/HomeStateMachineTest.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.presentation

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class HomeStateMachineTest : FunSpec({

    test("HomeState defaults every section to Loading and selectedTab to Home") {
        val state = HomeState()

        state.selectedTab shouldBe HomeTab.Home
        state.nowPlaying shouldBe SectionUiState.Loading
        state.latest shouldBe SectionUiState.Loading
        state.upcoming shouldBe SectionUiState.Loading
    }
})
```

- [ ] **Step 2: Run the test and verify it fails**

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"`
Expected: FAIL — `HomeState`, `HomeTab`, `SectionUiState` are unresolved.

- [ ] **Step 3: Implement `HomeContract.kt` (state shape + minimal `HomeEvent`)**

`feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/HomeContract.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.presentation

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.core.mvi.UiEffect
import com.utaputranto.joyviekmp.core.mvi.UiEvent
import com.utaputranto.joyviekmp.core.mvi.UiState

/** Bottom navigation destinations for the home feature. */
enum class HomeTab { Home, Search, Profile }

/** Independent loading/success/error state for a single movie section. */
sealed interface SectionUiState {
    data object Loading : SectionUiState

    data class Success(val movies: List<Movie>) : SectionUiState

    data class Error(val message: String) : SectionUiState
}

/**
 * Immutable UI state for the home feature.
 *
 * @param selectedTab Currently active bottom-nav tab.
 * @param nowPlaying State of the "Now Playing" movie section.
 * @param latest State of the "Latest" (top rated) movie section.
 * @param upcoming State of the "Upcoming" movie section.
 */
data class HomeState(
    val selectedTab: HomeTab = HomeTab.Home,
    val nowPlaying: SectionUiState = SectionUiState.Loading,
    val latest: SectionUiState = SectionUiState.Loading,
    val upcoming: SectionUiState = SectionUiState.Loading,
) : UiState

/** User intents and UI events for the home feature. */
sealed interface HomeEvent : UiEvent {
    data object LoadHomeMovies : HomeEvent
}

/** One-shot side effects for the home feature (none needed yet). */
sealed interface HomeEffect : UiEffect
```

- [ ] **Step 4: Run the test and verify it passes**

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"`
Expected: PASS (1 test green).

- [ ] **Step 5: Create the presentation-side `FakeHomeRepository`**

`commonTest` source sets are not shared across modules, so this mirrors (does not reuse) the domain fake from Task 2 — same pattern as `feature/onboarding/presentation/.../fakes/FakeOnboardingRepository` duplicating `feature/onboarding/domain/.../fakes/FakeOnboardingRepository`.

`feature/home/presentation/src/commonTest/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/fakes/FakeHomeRepository.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.presentation.fakes

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.repository.HomeRepository

class FakeHomeRepository : HomeRepository {
    private var nowPlayingResult: Result<List<Movie>> = Result.success(emptyList())
    private var topRatedResult: Result<List<Movie>> = Result.success(emptyList())
    private var upcomingResult: Result<List<Movie>> = Result.success(emptyList())

    var nowPlayingCallCount: Int = 0
        private set
    var topRatedCallCount: Int = 0
        private set
    var upcomingCallCount: Int = 0
        private set

    fun setNowPlayingResult(result: Result<List<Movie>>) {
        nowPlayingResult = result
    }

    fun setTopRatedResult(result: Result<List<Movie>>) {
        topRatedResult = result
    }

    fun setUpcomingResult(result: Result<List<Movie>>) {
        upcomingResult = result
    }

    override suspend fun getNowPlayingMovies(): Result<List<Movie>> {
        nowPlayingCallCount++
        return nowPlayingResult
    }

    override suspend fun getTopRatedMovies(): Result<List<Movie>> {
        topRatedCallCount++
        return topRatedResult
    }

    override suspend fun getUpcomingMovies(): Result<List<Movie>> {
        upcomingCallCount++
        return upcomingResult
    }
}
```

- [ ] **Step 6: Write the failing test for successful init load**

Add to `HomeStateMachineTest.kt` (replace the whole file with this expanded version — keeps the Step 1 test and adds shared fixtures + the new test):

```kotlin
package com.utaputranto.joyviekmp.feature.home.presentation

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.core.test.rules.MainDispatcherRule
import com.utaputranto.joyviekmp.feature.home.domain.usecase.GetNowPlayingMoviesUseCase
import com.utaputranto.joyviekmp.feature.home.domain.usecase.GetTopRatedMoviesUseCase
import com.utaputranto.joyviekmp.feature.home.domain.usecase.GetUpcomingMoviesUseCase
import com.utaputranto.joyviekmp.feature.home.presentation.fakes.FakeHomeRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class HomeStateMachineTest : FunSpec({
    val mainDispatcherRule = MainDispatcherRule()

    beforeSpec { mainDispatcherRule.starting() }
    afterSpec { mainDispatcherRule.finished() }

    val sampleMovie =
        Movie(
            id = 1,
            title = "Sample Movie",
            overview = "Overview",
            posterPath = "/sample.jpg",
            releaseDate = "2026-01-01",
            voteAverage = 7.5,
        )

    fun buildStateMachine(fakeRepository: FakeHomeRepository): HomeStateMachine =
        HomeStateMachine(
            GetNowPlayingMoviesUseCase(fakeRepository),
            GetTopRatedMoviesUseCase(fakeRepository),
            GetUpcomingMoviesUseCase(fakeRepository),
        )

    test("HomeState defaults every section to Loading and selectedTab to Home") {
        val state = HomeState()

        state.selectedTab shouldBe HomeTab.Home
        state.nowPlaying shouldBe SectionUiState.Loading
        state.latest shouldBe SectionUiState.Loading
        state.upcoming shouldBe SectionUiState.Loading
    }

    test("init loads all three sections to Success independently") {
        val fakeRepository = FakeHomeRepository()
        fakeRepository.setNowPlayingResult(Result.success(listOf(sampleMovie)))
        fakeRepository.setTopRatedResult(Result.success(listOf(sampleMovie)))
        fakeRepository.setUpcomingResult(Result.success(listOf(sampleMovie)))

        val stateMachine = buildStateMachine(fakeRepository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        stateMachine.state.value.nowPlaying shouldBe SectionUiState.Success(listOf(sampleMovie))
        stateMachine.state.value.latest shouldBe SectionUiState.Success(listOf(sampleMovie))
        stateMachine.state.value.upcoming shouldBe SectionUiState.Success(listOf(sampleMovie))
    }
})
```

- [ ] **Step 7: Run the test and verify it fails**

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"`
Expected: FAIL — `HomeStateMachine` is unresolved (class doesn't exist yet).

- [ ] **Step 8: Implement minimal `HomeStateMachine` (success path only)**

`feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/HomeStateMachine.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.utaputranto.joyviekmp.core.mvi.BaseStateMachine
import com.utaputranto.joyviekmp.feature.home.domain.usecase.GetNowPlayingMoviesUseCase
import com.utaputranto.joyviekmp.feature.home.domain.usecase.GetTopRatedMoviesUseCase
import com.utaputranto.joyviekmp.feature.home.domain.usecase.GetUpcomingMoviesUseCase
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeStateMachine(
    private val getNowPlayingMovies: GetNowPlayingMoviesUseCase,
    private val getTopRatedMovies: GetTopRatedMoviesUseCase,
    private val getUpcomingMovies: GetUpcomingMoviesUseCase,
) : BaseStateMachine<HomeState, HomeEvent, HomeEffect>(HomeState()) {
    init {
        onEvent(HomeEvent.LoadHomeMovies)
    }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadHomeMovies -> loadAllSections()
        }
    }

    private fun loadAllSections() {
        loadNowPlaying()
        loadLatest()
        loadUpcoming()
    }

    private fun loadNowPlaying() {
        viewModelScope.launch {
            val movies = getNowPlayingMovies().getOrThrow()
            setState { copy(nowPlaying = SectionUiState.Success(movies)) }
        }
    }

    private fun loadLatest() {
        viewModelScope.launch {
            val movies = getTopRatedMovies().getOrThrow()
            setState { copy(latest = SectionUiState.Success(movies)) }
        }
    }

    private fun loadUpcoming() {
        viewModelScope.launch {
            val movies = getUpcomingMovies().getOrThrow()
            setState { copy(upcoming = SectionUiState.Success(movies)) }
        }
    }
}
```

- [ ] **Step 9: Run the test and verify it passes**

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"`
Expected: PASS (2 tests green).

- [ ] **Step 10: Write the failing test for independent per-section failure**

Add to `HomeStateMachineTest.kt`, after the `"init loads all three sections to Success independently"` test:

```kotlin
    test("one section failing sets only that section to Error, others stay Success") {
        val fakeRepository = FakeHomeRepository()
        fakeRepository.setNowPlayingResult(Result.failure(RuntimeException("now playing failed")))
        fakeRepository.setTopRatedResult(Result.success(listOf(sampleMovie)))
        fakeRepository.setUpcomingResult(Result.success(listOf(sampleMovie)))

        val stateMachine = buildStateMachine(fakeRepository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        stateMachine.state.value.nowPlaying shouldBe SectionUiState.Error("now playing failed")
        stateMachine.state.value.latest shouldBe SectionUiState.Success(listOf(sampleMovie))
        stateMachine.state.value.upcoming shouldBe SectionUiState.Success(listOf(sampleMovie))
    }
```

- [ ] **Step 11: Run the test and verify it fails**

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"`
Expected: FAIL — `nowPlaying` state stays `Loading` (the `getOrThrow()` exception is never turned into `SectionUiState.Error`; the coroutine simply fails silently).

- [ ] **Step 12: Implement proper success/failure handling for all three sections**

In `HomeStateMachine.kt`, replace the three `loadX` functions with:

```kotlin
    private fun loadNowPlaying() {
        viewModelScope.launch {
            getNowPlayingMovies().fold(
                onSuccess = { movies -> setState { copy(nowPlaying = SectionUiState.Success(movies)) } },
                onFailure = { error ->
                    setState { copy(nowPlaying = SectionUiState.Error(error.message ?: DEFAULT_ERROR_MESSAGE)) }
                },
            )
        }
    }

    private fun loadLatest() {
        viewModelScope.launch {
            getTopRatedMovies().fold(
                onSuccess = { movies -> setState { copy(latest = SectionUiState.Success(movies)) } },
                onFailure = { error ->
                    setState { copy(latest = SectionUiState.Error(error.message ?: DEFAULT_ERROR_MESSAGE)) }
                },
            )
        }
    }

    private fun loadUpcoming() {
        viewModelScope.launch {
            getUpcomingMovies().fold(
                onSuccess = { movies -> setState { copy(upcoming = SectionUiState.Success(movies)) } },
                onFailure = { error ->
                    setState { copy(upcoming = SectionUiState.Error(error.message ?: DEFAULT_ERROR_MESSAGE)) }
                },
            )
        }
    }

    private companion object {
        const val DEFAULT_ERROR_MESSAGE = "Something went wrong"
    }
```

- [ ] **Step 13: Run the test and verify it passes**

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"`
Expected: PASS (3 tests green — the earlier success-path test still passes unchanged).

- [ ] **Step 14: Write the failing test for `SelectTab`**

Add to `HomeStateMachineTest.kt`:

```kotlin
    test("SelectTab updates selectedTab without re-invoking any use case") {
        val fakeRepository = FakeHomeRepository()
        val stateMachine = buildStateMachine(fakeRepository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val nowPlayingCallsBefore = fakeRepository.nowPlayingCallCount
        val topRatedCallsBefore = fakeRepository.topRatedCallCount
        val upcomingCallsBefore = fakeRepository.upcomingCallCount

        stateMachine.onEvent(HomeEvent.SelectTab(HomeTab.Search))
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        stateMachine.state.value.selectedTab shouldBe HomeTab.Search
        fakeRepository.nowPlayingCallCount shouldBe nowPlayingCallsBefore
        fakeRepository.topRatedCallCount shouldBe topRatedCallsBefore
        fakeRepository.upcomingCallCount shouldBe upcomingCallsBefore
    }
```

- [ ] **Step 15: Run the test and verify it fails**

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"`
Expected: FAIL — compile error, `HomeEvent.SelectTab` is unresolved.

- [ ] **Step 16: Implement `SelectTab`**

In `HomeContract.kt`, add the case to `HomeEvent`:

```kotlin
sealed interface HomeEvent : UiEvent {
    data object LoadHomeMovies : HomeEvent

    data class SelectTab(val tab: HomeTab) : HomeEvent
}
```

In `HomeStateMachine.kt`, add the branch to `onEvent`:

```kotlin
    override fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadHomeMovies -> loadAllSections()
            is HomeEvent.SelectTab -> setState { copy(selectedTab = event.tab) }
        }
    }
```

- [ ] **Step 17: Run the test and verify it passes**

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"`
Expected: PASS (4 tests green).

- [ ] **Step 18: Write the failing test for `RetryNowPlaying`**

Add to `HomeStateMachineTest.kt`:

```kotlin
    test("RetryNowPlaying re-invokes only the now playing use case") {
        val fakeRepository = FakeHomeRepository()
        val stateMachine = buildStateMachine(fakeRepository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val nowPlayingCallsBefore = fakeRepository.nowPlayingCallCount
        val topRatedCallsBefore = fakeRepository.topRatedCallCount
        val upcomingCallsBefore = fakeRepository.upcomingCallCount

        stateMachine.onEvent(HomeEvent.RetryNowPlaying)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        fakeRepository.nowPlayingCallCount shouldBe nowPlayingCallsBefore + 1
        fakeRepository.topRatedCallCount shouldBe topRatedCallsBefore
        fakeRepository.upcomingCallCount shouldBe upcomingCallsBefore
    }
```

- [ ] **Step 19: Run the test and verify it fails**

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"`
Expected: FAIL — compile error, `HomeEvent.RetryNowPlaying` is unresolved.

- [ ] **Step 20: Implement `RetryNowPlaying`**

In `HomeContract.kt`, add to `HomeEvent`:

```kotlin
    data object RetryNowPlaying : HomeEvent
```

In `HomeStateMachine.kt`, add the branch:

```kotlin
            HomeEvent.RetryNowPlaying -> loadNowPlaying()
```

- [ ] **Step 21: Run the test and verify it passes**

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"`
Expected: PASS (5 tests green).

- [ ] **Step 22: Write the failing test for `RetryLatest`**

Add to `HomeStateMachineTest.kt`:

```kotlin
    test("RetryLatest re-invokes only the top rated use case") {
        val fakeRepository = FakeHomeRepository()
        val stateMachine = buildStateMachine(fakeRepository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val nowPlayingCallsBefore = fakeRepository.nowPlayingCallCount
        val topRatedCallsBefore = fakeRepository.topRatedCallCount
        val upcomingCallsBefore = fakeRepository.upcomingCallCount

        stateMachine.onEvent(HomeEvent.RetryLatest)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        fakeRepository.topRatedCallCount shouldBe topRatedCallsBefore + 1
        fakeRepository.nowPlayingCallCount shouldBe nowPlayingCallsBefore
        fakeRepository.upcomingCallCount shouldBe upcomingCallsBefore
    }
```

- [ ] **Step 23: Run the test, verify it fails, implement `RetryLatest`, verify it passes**

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"` — expect FAIL (`HomeEvent.RetryLatest` unresolved).

In `HomeContract.kt`, add to `HomeEvent`:

```kotlin
    data object RetryLatest : HomeEvent
```

In `HomeStateMachine.kt`, add the branch:

```kotlin
            HomeEvent.RetryLatest -> loadLatest()
```

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"`
Expected: PASS (6 tests green).

- [ ] **Step 24: Write the failing test for `RetryUpcoming`**

Add to `HomeStateMachineTest.kt`:

```kotlin
    test("RetryUpcoming re-invokes only the upcoming use case") {
        val fakeRepository = FakeHomeRepository()
        val stateMachine = buildStateMachine(fakeRepository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val nowPlayingCallsBefore = fakeRepository.nowPlayingCallCount
        val topRatedCallsBefore = fakeRepository.topRatedCallCount
        val upcomingCallsBefore = fakeRepository.upcomingCallCount

        stateMachine.onEvent(HomeEvent.RetryUpcoming)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        fakeRepository.upcomingCallCount shouldBe upcomingCallsBefore + 1
        fakeRepository.nowPlayingCallCount shouldBe nowPlayingCallsBefore
        fakeRepository.topRatedCallCount shouldBe topRatedCallsBefore
    }
```

- [ ] **Step 25: Run the test, verify it fails, implement `RetryUpcoming`, verify it passes**

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"` — expect FAIL (`HomeEvent.RetryUpcoming` unresolved).

In `HomeContract.kt`, add to `HomeEvent`:

```kotlin
    data object RetryUpcoming : HomeEvent
```

In `HomeStateMachine.kt`, add the branch:

```kotlin
            HomeEvent.RetryUpcoming -> loadUpcoming()
```

Run: `./gradlew :feature:home:presentation:test --tests "*.HomeStateMachineTest"`
Expected: PASS (7 tests green).

- [ ] **Step 26: Create the presentation Koin module aggregator**

`feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/di/HomePresentationModule.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.presentation.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

/** Aggregates annotated home-presentation definitions (@KoinViewModel) into a module hint. */
@Module
@ComponentScan("com.utaputranto.joyviekmp.feature.home.presentation")
class HomePresentationModule
```

- [ ] **Step 27: Run the full presentation test suite and verify all pass**

Run: `./gradlew :feature:home:presentation:test`
Expected: PASS (7 tests green).

- [ ] **Step 28: Commit**

```bash
git add feature/home/presentation
git commit -m "feat(home): add HomeStateMachine with per-section loading state"
```

---

### Task 5: Design system — bottom nav bar and movie section components

**Files:**
- Create: `core/designsystem/src/commonMain/kotlin/com/utaputranto/joyviekmp/core/designsystem/atom/JoyvieBottomNavBar.kt`
- Create: `core/designsystem/src/commonMain/kotlin/com/utaputranto/joyviekmp/core/designsystem/molecule/JoyvieMovieSection.kt`

**Interfaces:**
- Consumes: `JoyvieTheme.colors/dimens/typography`, `JoyviePosterCard(imageUrl, modifier, contentDescription, onClick)` (existing), `SectionUiState` (Task 4 — `JoyvieMovieSection` takes a generic list + loading/error flags rather than importing the presentation-module type directly, since `core:designsystem` cannot depend on a feature module).
- Produces: `data class JoyvieBottomNavItem(val label: String, val icon: (@Composable () -> Unit)? = null)`; `@Composable fun JoyvieBottomNavBar(items: List<JoyvieBottomNavItem>, selectedIndex: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier)`; `@Composable fun JoyvieMovieSection(title: String, movies: List<MovieSectionItem>, isLoading: Boolean, errorMessage: String?, onRetry: () -> Unit, onMovieClick: (MovieSectionItem) -> Unit, modifier: Modifier = Modifier)` with `data class MovieSectionItem(val id: Int, val imageUrl: String, val title: String)`.

No unit tests for this task (Compose UI, no test precedent in this codebase — see Global Constraints). Verified via Spotless + build + Compose preview.

- [ ] **Step 1: Implement `JoyvieBottomNavBar`**

`core/designsystem/src/commonMain/kotlin/com/utaputranto/joyviekmp/core/designsystem/atom/JoyvieBottomNavBar.kt`:

```kotlin
package com.utaputranto.joyviekmp.core.designsystem.atom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

/**
 * Bottom navigation bar atom for the Joyvie Design System.
 * Generic — the caller supplies its own tab items, so it's reusable across features.
 */
data class JoyvieBottomNavItem(
    val label: String,
    val icon: (@Composable () -> Unit)? = null,
)

@Composable
fun JoyvieBottomNavBar(
    items: List<JoyvieBottomNavItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = JoyvieTheme.colors.surface,
        contentColor = JoyvieTheme.colors.onSurface,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = { onSelect(index) },
                icon = { item.icon?.invoke() },
                label = { Text(text = item.label, style = JoyvieTheme.typography.caption) },
                selectedIconColor = JoyvieTheme.colors.primary,
                selectedTextColor = JoyvieTheme.colors.primary,
                unselectedIconColor = JoyvieTheme.colors.onSurfaceVariant,
                unselectedTextColor = JoyvieTheme.colors.onSurfaceVariant,
            )
        }
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
private fun JoyvieBottomNavBarPreview(
    @androidx.compose.ui.tooling.preview.PreviewParameter(
        com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider::class,
    ) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        Column {
            JoyvieBottomNavBar(
                items =
                    listOf(
                        JoyvieBottomNavItem("Home"),
                        JoyvieBottomNavItem("Search"),
                        JoyvieBottomNavItem("Profile"),
                    ),
                selectedIndex = 0,
                onSelect = {},
                modifier = Modifier.padding(JoyvieTheme.dimens.spacing.none),
            )
        }
    }
}
```

Note: check the installed `compose-material3` version's `NavigationBarItem` parameter names (`selectedIconColor` etc. are on `NavigationBarItemColors` in some versions via a `colors = NavigationBarItemDefaults.colors(...)` parameter instead of flat parameters) — if the flat named parameters don't resolve, replace them with:

```kotlin
                colors =
                    androidx.compose.material3.NavigationBarItemDefaults.colors(
                        selectedIconColor = JoyvieTheme.colors.primary,
                        selectedTextColor = JoyvieTheme.colors.primary,
                        unselectedIconColor = JoyvieTheme.colors.onSurfaceVariant,
                        unselectedTextColor = JoyvieTheme.colors.onSurfaceVariant,
                    ),
```

- [ ] **Step 2: Implement `JoyvieMovieSection`**

`core/designsystem/src/commonMain/kotlin/com/utaputranto/joyviekmp/core/designsystem/molecule/JoyvieMovieSection.kt`:

```kotlin
package com.utaputranto.joyviekmp.core.designsystem.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyviePosterCard
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

data class MovieSectionItem(
    val id: Int,
    val imageUrl: String,
    val title: String,
)

/**
 * Horizontal movie list molecule for the Joyvie Design System.
 * Renders loading/error/content states for one titled section (e.g. "Now Playing").
 */
@Composable
fun JoyvieMovieSection(
    title: String,
    movies: List<MovieSectionItem>,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onMovieClick: (MovieSectionItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = JoyvieTheme.typography.title,
            color = JoyvieTheme.colors.onBackground,
            modifier = Modifier.padding(horizontal = JoyvieTheme.dimens.spacing.medium),
        )
        Spacer(JoyvieTheme.dimens.spacing.small)
        when {
            isLoading ->
                CircularProgressIndicator(
                    modifier = Modifier.padding(horizontal = JoyvieTheme.dimens.spacing.medium),
                    color = JoyvieTheme.colors.primary,
                )

            errorMessage != null ->
                Row(modifier = Modifier.padding(horizontal = JoyvieTheme.dimens.spacing.medium)) {
                    Text(
                        text = errorMessage,
                        style = JoyvieTheme.typography.body,
                        color = JoyvieTheme.colors.error,
                    )
                    TextButton(onClick = onRetry) {
                        Text(text = "Retry", style = JoyvieTheme.typography.body)
                    }
                }

            else ->
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(JoyvieTheme.dimens.spacing.small),
                    contentPadding =
                        androidx.compose.foundation.layout.PaddingValues(
                            horizontal = JoyvieTheme.dimens.spacing.medium,
                        ),
                ) {
                    items(movies, key = { it.id }) { movie ->
                        JoyviePosterCard(
                            imageUrl = movie.imageUrl,
                            contentDescription = movie.title,
                            onClick = { onMovieClick(movie) },
                        )
                    }
                }
        }
    }
}

@Composable
private fun Spacer(height: androidx.compose.ui.unit.Dp) {
    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = height))
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
private fun JoyvieMovieSectionPreview(
    @androidx.compose.ui.tooling.preview.PreviewParameter(
        com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider::class,
    ) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        JoyvieMovieSection(
            title = "Now Playing",
            movies =
                listOf(
                    MovieSectionItem(1, "https://example.com/poster1.jpg", "Movie 1"),
                    MovieSectionItem(2, "https://example.com/poster2.jpg", "Movie 2"),
                ),
            isLoading = false,
            errorMessage = null,
            onRetry = {},
            onMovieClick = {},
        )
    }
}
```

- [ ] **Step 3: Verify the module builds and Spotless passes**

Run: `./gradlew :core:designsystem:spotlessApply :core:designsystem:build`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 4: Commit**

```bash
git add core/designsystem
git commit -m "feat(designsystem): add JoyvieBottomNavBar and JoyvieMovieSection"
```

---

### Task 6: Home screen composition and navigation wiring

**Files:**
- Create: `feature/home/api/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/api/navigation/HomeNavigationApi.kt`
- Create: `feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/screen/home/HomeScreen.kt`
- Create: `feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/screen/search/SearchPlaceholderScreen.kt`
- Create: `feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/screen/profile/ProfilePlaceholderScreen.kt`
- Create: `feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/navigation/HomeNavigation.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/utaputranto/joyviekmp/AppNavigation.kt`
- Modify: `feature/onboarding/presentation/build.gradle.kts`
- Modify: `feature/onboarding/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/onboarding/presentation/navigation/OnboardingNavigation.kt`

**Interfaces:**
- Consumes: `HomeState`, `HomeEvent`, `HomeStateMachine` (Task 4); `JoyvieBottomNavBar`, `JoyvieBottomNavItem`, `JoyvieMovieSection`, `MovieSectionItem` (Task 5); `SectionUiState` (Task 4).
- Produces: `@Serializable data object HomeRoute : NavKey`; `fun MutableList<NavKey>.navigateToHome()`; `fun EntryProviderScope<NavKey>.homeEntries(backStack: MutableList<NavKey>)`.

No unit tests for this task (Compose UI + navigation wiring — see Global Constraints). Verified via build success and a manual run.

- [ ] **Step 1: Create the home route API**

`feature/home/api/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/api/navigation/HomeNavigationApi.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute : NavKey

fun MutableList<NavKey>.navigateToHome() {
    clear()
    add(HomeRoute)
}
```

- [ ] **Step 2: Implement `HomeScreen`**

`feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/screen/home/HomeScreen.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieBottomNavBar
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieBottomNavItem
import com.utaputranto.joyviekmp.core.designsystem.molecule.JoyvieMovieSection
import com.utaputranto.joyviekmp.core.designsystem.molecule.MovieSectionItem
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.presentation.HomeEvent
import com.utaputranto.joyviekmp.feature.home.presentation.HomeState
import com.utaputranto.joyviekmp.feature.home.presentation.HomeTab
import com.utaputranto.joyviekmp.feature.home.presentation.SectionUiState
import com.utaputranto.joyviekmp.feature.home.presentation.screen.profile.ProfilePlaceholderScreen
import com.utaputranto.joyviekmp.feature.home.presentation.screen.search.SearchPlaceholderScreen

private val TABS =
    listOf(
        JoyvieBottomNavItem("Home"),
        JoyvieBottomNavItem("Search"),
        JoyvieBottomNavItem("Profile"),
    )

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JoyvieTheme.colors.background,
        bottomBar = {
            JoyvieBottomNavBar(
                items = TABS,
                selectedIndex = state.selectedTab.ordinal,
                onSelect = { index -> onEvent(HomeEvent.SelectTab(HomeTab.entries[index])) },
            )
        },
    ) { innerPadding ->
        when (state.selectedTab) {
            HomeTab.Home ->
                HomeTabContent(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                )

            HomeTab.Search -> SearchPlaceholderScreen(modifier = Modifier.fillMaxSize().padding(innerPadding))
            HomeTab.Profile -> ProfilePlaceholderScreen(modifier = Modifier.fillMaxSize().padding(innerPadding))
        }
    }
}

@Composable
private fun HomeTabContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(JoyvieTheme.dimens.spacing.large),
    ) {
        JoyvieMovieSection(
            title = "Now Playing",
            movies = state.nowPlaying.toSectionItems(),
            isLoading = state.nowPlaying is SectionUiState.Loading,
            errorMessage = (state.nowPlaying as? SectionUiState.Error)?.message,
            onRetry = { onEvent(HomeEvent.RetryNowPlaying) },
            onMovieClick = {},
        )
        JoyvieMovieSection(
            title = "Latest",
            movies = state.latest.toSectionItems(),
            isLoading = state.latest is SectionUiState.Loading,
            errorMessage = (state.latest as? SectionUiState.Error)?.message,
            onRetry = { onEvent(HomeEvent.RetryLatest) },
            onMovieClick = {},
        )
        JoyvieMovieSection(
            title = "Upcoming",
            movies = state.upcoming.toSectionItems(),
            isLoading = state.upcoming is SectionUiState.Loading,
            errorMessage = (state.upcoming as? SectionUiState.Error)?.message,
            onRetry = { onEvent(HomeEvent.RetryUpcoming) },
            onMovieClick = {},
        )
    }
}

private fun SectionUiState.toSectionItems(): List<MovieSectionItem> =
    (this as? SectionUiState.Success)?.movies?.map { it.toSectionItem() } ?: emptyList()

private fun Movie.toSectionItem(): MovieSectionItem =
    MovieSectionItem(
        id = id,
        imageUrl = "https://image.tmdb.org/t/p/w342$posterPath",
        title = title,
    )
```

- [ ] **Step 3: Implement the Search and Profile placeholders**

`feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/screen/search/SearchPlaceholderScreen.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.presentation.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

@Composable
fun SearchPlaceholderScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Search coming soon",
            style = JoyvieTheme.typography.body,
            color = JoyvieTheme.colors.onSurfaceVariant,
        )
    }
}
```

`feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/screen/profile/ProfilePlaceholderScreen.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.presentation.screen.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

@Composable
fun ProfilePlaceholderScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Profile coming soon",
            style = JoyvieTheme.typography.body,
            color = JoyvieTheme.colors.onSurfaceVariant,
        )
    }
}
```

- [ ] **Step 4: Wire the Nav3 entry**

`feature/home/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/home/presentation/navigation/HomeNavigation.kt`:

```kotlin
package com.utaputranto.joyviekmp.feature.home.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.utaputranto.joyviekmp.feature.home.api.navigation.HomeRoute
import com.utaputranto.joyviekmp.feature.home.presentation.HomeStateMachine
import com.utaputranto.joyviekmp.feature.home.presentation.screen.home.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

/**
 * Registers this feature's screen as a Nav3 entry against a shared back stack.
 * Called from the app-level NavDisplay entryProvider.
 */
fun EntryProviderScope<NavKey>.homeEntries(backStack: MutableList<NavKey>) {
    entry<HomeRoute> {
        val stateMachine = koinViewModel<HomeStateMachine>()
        val state by stateMachine.state.collectAsStateWithLifecycle()

        HomeScreen(
            state = state,
            onEvent = stateMachine::onEvent,
        )
    }
}
```

- [ ] **Step 5: Register `homeEntries` in `AppNavigation.kt`**

In `composeApp/src/commonMain/kotlin/com/utaputranto/joyviekmp/AppNavigation.kt`, add the import:

```kotlin
import com.utaputranto.joyviekmp.feature.home.presentation.navigation.homeEntries
```

and add `homeEntries(backStack)` inside the `entryProvider { }` block, alongside the existing entries:

```kotlin
        entryProvider =
            entryProvider {
                onboardingEntries(backStack, viewModelStoreOwner)
                authEntries(backStack)
                homeEntries(backStack)
            },
```

- [ ] **Step 6: Let onboarding navigate to home**

In `feature/onboarding/presentation/build.gradle.kts`, add the cross-feature dependency next to the existing auth one:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            // Cross-feature: navigate forward to auth
            implementation(projects.feature.auth.api)
            // Cross-feature: navigate forward to home
            implementation(projects.feature.home.api)
        }
    }
}
```

In `feature/onboarding/presentation/src/commonMain/kotlin/com/utaputranto/joyviekmp/feature/onboarding/presentation/navigation/OnboardingNavigation.kt`, add the import:

```kotlin
import com.utaputranto.joyviekmp.feature.home.api.navigation.navigateToHome
```

and replace the TODO branch:

```kotlin
                OnboardingEffect.NavigateToHome -> {
                    // TODO: Navigate to Home feature when Home API is available
                }
```

with:

```kotlin
                OnboardingEffect.NavigateToHome -> backStack.navigateToHome()
```

- [ ] **Step 7: Build the full app**

Run: `./gradlew spotlessApply :composeApp:build :androidApp:assembleDebug`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 8: Manually verify on Android**

Run: `./gradlew :androidApp:installDebug`, launch the app, complete or skip onboarding until it reaches Home. Confirm: bottom nav shows Home/Search/Profile; Home tab shows three horizontal movie sections (each independently shows a spinner then posters, or an error+Retry if the TMDB token is missing/invalid); tapping Search/Profile shows the placeholder text and switches back correctly; tapping Retry on a section re-triggers only that section's spinner.

- [ ] **Step 9: Commit**

```bash
git add feature/home composeApp feature/onboarding
git commit -m "feat(home): wire HomeScreen, bottom nav, and navigation from onboarding"
```

---

### Task 7: Full verification pass

**Files:** none (verification only).

- [ ] **Step 1: Run the full test suite**

Run: `./gradlew test`
Expected: BUILD SUCCESSFUL, all modules' tests green (including the new `feature:home:domain`, `feature:home:data`, `feature:home:presentation` suites).

- [ ] **Step 2: Run the full CI-equivalent build**

Run: `./gradlew spotlessCheck test assembleDebug --continue`
Expected: BUILD SUCCESSFUL.

- [ ] **Step 3: Fix any Spotless/ktlint violations if Step 2 reported them**

Run: `./gradlew spotlessApply`, then re-run Step 2 to confirm clean.

- [ ] **Step 4: Commit if Step 3 made changes**

```bash
git add -A
git commit -m "chore(home): apply spotless formatting"
```
