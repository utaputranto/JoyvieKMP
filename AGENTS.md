# AGENTS.md

This file provides guidance to coding agents (Claude Code, etc.) when working with code in this repository.

## Project

JoyvieKMP: Kotlin Multiplatform (Android + iOS) app using Compose Multiplatform, Koin DI, Ktor,
and Public TMDB API. Package root: `com.utaputranto.joyviekmp`.

## Common commands

Run from the `JoyvieKMP/` directory (the actual Gradle root — the repo root above it is not a
Gradle project).

- Build Android debug APK: `./gradlew :androidApp:assembleDebug`
- Run all tests: `./gradlew test`
- Run tests for one module: `./gradlew :feature:onboarding:domain:test` (or `:shared:testAndroidHostTest` /
  `:shared:iosSimulatorArm64Test` style tasks per module where applicable)
- Lint/format check: `./gradlew spotlessCheck`
- Auto-format: `./gradlew spotlessApply`
- Full CI-equivalent run: `./gradlew spotlessCheck test assembleDebug sonar --continue`
- iOS app: open `iosApp/` in Xcode and run from there.

TMDB API token: set `TMDB_TOKEN` env var, or add `tmdb.token=...` to `local.properties`
(git-ignored). `core/network`'s `generateBuildConfig` Gradle task writes it into a generated
`BuildConfig.kt` at build time — env var wins if both are present.

## Module architecture

Gradle module graph is a layered clean-architecture split by feature, wired together with custom
convention plugins in `build-logic/convention` (applied via `alias(libs.plugins.joyvie.*)`, not
hand-written `build.gradle.kts` boilerplate). Understanding the plugins is the fastest way to
understand what each module is allowed to depend on:

- `joyvie.kmp.library` (`KmpLibraryConventionPlugin`) — base: Android + iOS targets, serialization,
  namespace auto-derived from the Gradle path (`:core:model` → `com.utaputranto.joyviekmp.core.model`).
- `joyvie.kmp.feature` (`KmpFeatureConventionPlugin`) — above + Compose Multiplatform.
- `joyvie.feature.api` — route objects (`@Serializable`) + `NavController` extension functions for
  navigating into the feature. No domain/data deps. Other features depend on a feature's `api`
  module to navigate to it without pulling in its implementation.
- `joyvie.feature.domain` — repository interfaces + use cases, pure Kotlin, depends only on
  `:core:model`.
- `joyvie.feature.data` — repository implementations; auto-depends on the sibling `domain` module,
  `:core:model`, `:core:network`.
- `joyvie.feature.presentation` — Compose screens, ViewModel, nav graph; auto-depends on sibling
  `api` + `domain`, `:core:model`, `:core:platform`, `:core:designsystem`. Cross-feature
  presentation deps (e.g. onboarding navigating to auth) are declared explicitly in that module's
  own `build.gradle.kts` against the *other* feature's `api` module only.
- `joyvie.kmp.application` (`ComposeAppConventionPlugin`, used by `composeApp`) — auto-depends on
  **every** `:core:*` module and every `:feature:*:presentation`/`:data`/`:api` module by scanning
  `rootProject.subprojects`. Adding a new feature module wires it into the app automatically; you
  do not need to touch `composeApp/build.gradle.kts`.

Each feature (`auth`, `onboarding`) therefore has 4 Gradle modules: `api`, `domain`, `data`,
`presentation`. When adding a new feature, replicate this 4-module shape and register all four in
`settings.gradle.kts`.

### Koin DI wiring is generated, not hand-assembled

`composeApp`'s `generateKoinModules` Gradle task (in `ComposeAppConventionPlugin.kt`) scans every
`:core:*`, `*:presentation`, `*:data`, `*:api` module's `src/commonMain/kotlin/**/di/*.kt` for
`val xModule = module { ... }` declarations and generates
`com.utaputranto.joyviekmp.di.GeneratedModules.kt` containing `generatedModules: List<Module>`.
`composeApp/.../di/AppModule.kt` combines that with `platformModule` into `appModules`, started once
by `initKoin()`. **Implication:** to register a new Koin module, just declare
`val fooModule = module { ... }` inside a `di/` package in one of the scanned module types — no
manual registration needed anywhere else. The generator is a plain regex over source text, not a
compiler, so keep the `val <name>Module = module { ... }` shape recognizable.

### Navigation

Each feature's `api` module declares `@Serializable` route objects and `NavController.navigateToX()`
extensions (type-safe Navigation Compose routes). Each feature's `presentation` module declares a
`NavGraphBuilder.xGraph(navController)` extension. `composeApp/.../AppNavigation.kt` composes these
graphs into one `NavHost` and is the one place that lists every feature's graph explicitly.

### core modules

- `core:model` — plain data classes shared across features.
- `core:network` — Ktor `HttpClient` factory (`TmdbHttpClientFactory`), TMDB error mapping
  (`ApiException` subclasses by HTTP status), DTOs, `SafeApiCall` wrapper, generated `BuildConfig`.
- `core:designsystem` — Compose theme (`JoyvieTheme`, colors, typography, dimens) and atoms
  (`JoyvieButton`, `JoyvieTextField`, `JoyviePosterCard`).
- `core:platform` — `expect`/`actual` platform abstractions (`Logger`, `DeviceInfo`, `Platform`,
  `PlatformNotifier`) with `androidMain`/`iosMain` implementations.

## Code style

Kotlin formatting/lint is enforced by Spotless + ktlint (`joyvie.spotless`, applied at the root and
inherited by all subprojects) — run `spotlessApply` before committing if `spotlessCheck` fails.
