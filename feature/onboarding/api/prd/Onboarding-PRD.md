# Product Requirements Document (PRD)

## Onboarding Feature — Joyvie Movie Application

|              |                                       |
|--------------|---------------------------------------|
| **Document** | PRD-Onboarding-001                    |
| **Status**   | Draft                                 |
| **Type**     | Feature / User Story                  |
| **Priority** | High                                  |
| **Platform** | Mobile (Android & iOS)                |
| **Labels**   | mobile, ui/ux, onboarding, Joyvie-app |
| **Date**     | July 20, 2026                         |

---

## 1. Background & Context

When a user opens the Joyvie Movie Application for the first time, there is currently no mechanism
to introduce the app's core features (trending discovery, search/filtering, and watchlists). Without
onboarding, new users risk landing directly on the Home Screen with no context, which can reduce
feature understanding and early retention.

This feature introduces a brief **Splash Screen** experience followed by a **3-page Welcome Screen
carousel**, with completion state persisted locally so it does not reappear for returning users.

**Scope boundary:** This feature is entirely client-side. No backend, API, or authentication changes
are required.

---

## 2. Goals & Business Value

- Provide a smooth launch experience while local app initialization completes.
- Introduce core movie discovery capabilities in three concise steps.
- Help users understand trending content, search & filter, and watchlist functionality from the
  start.
- Reduce friction for returning users by bypassing onboarding after completion.
- Ensure onboarding state remains available offline through local persistence.

### Success Metrics

*(to be validated/filled in with Product & Data teams — no baseline defined in the original ticket)*

| Metric                                                   | Initial Target (proposed) |
|----------------------------------------------------------|---------------------------|
| Onboarding completion rate (reaches Get Started/Skip)    | ≥ 85%                     |
| Drop-off rate per carousel page                          | < 10% per page            |
| Crash/error rate during splash-to-destination transition | 0%                        |
| Splash screen duration (cold start)                      | ≤ 2 seconds               |

---

## 3. User Story

> As a movie enthusiast opening the app for the first time,
> I want to see a quick splash screen followed by a three-page welcome carousel,
> So that I can understand the app's key features before exploring movies.

### Persona

- **Primary:** New user (first-time user), has never opened the app before.
- **Secondary:** Returning user — must not see onboarding again unless the state is intentionally
  reset.

---

## 4. Scope

### 4.1 In Scope

- Splash Screen that reads the local `hasSeenOnboarding` state.
- 3-page Welcome Carousel (swipeable, horizontal pager).
- Local persistence (DataStore / SharedPreferences / MMKV / Async Storage — choose one).
- Skip, Next, and Get Started navigation.
- Page indicator.
- Android & iOS phone support (screen sizes and orientations per product requirements).

### 4.2 Out of Scope

- Any backend, API, or server-side changes.
- User authentication/login.
- Onboarding content personalization based on user data.
- Tablet/large-screen layouts (unless otherwise stated by the Design team).
- Analytics/event tracking (not mentioned in the original ticket — needs clarification on whether
  it's required).

---

## 5. Functional Specification

### 5.1 Splash Screen

| No   | Requirement                                                                                  |
|------|----------------------------------------------------------------------------------------------|
| SP-1 | Display the Joyvie Movie App logo and branding on cold launch                                |
| SP-2 | Read the local `hasSeenOnboarding` value before routing                                      |
| SP-3 | Route to Welcome Screen when `hasSeenOnboarding == false`                                    |
| SP-4 | Route directly to Home Screen when `hasSeenOnboarding == true`                               |
| SP-5 | Keep the splash experience brief and visually consistent across Android and iOS              |
| SP-6 | If local state is missing or unreadable, default to `hasSeenOnboarding == false` (fail-safe) |

### 5.2 Welcome Carousel (3 Pages)

| Page  | Content                                                                                         | CTA             |
|-------|-------------------------------------------------------------------------------------------------|-----------------|
| **1** | Discover Trending Movies & TV Shows — browsing trending & popular content, movie-themed imagery | Next            |
| **2** | Search & Filter Content — search by title, genre, rating, popularity                            | Next            |
| **3** | Create Watchlists & Track Favorites — how to save movies and manage a personal watchlist        | **Get Started** |

### 5.3 UI & Interactions

- Visual style: Joyvie-inspired dark mode and light mode.
- Full support for Android & iOS phones (portrait, and other orientations per requirements).
- 3 page indicators that clearly identify the current page.
- **Skip** button in the top corner, visible on all welcome pages.
- Primary **Next** button on Pages 1 & 2; changes to **Get Started** on Page 3.
- Navigation via swipe or tapping the primary button.
- Touch targets, contrast, and labels must meet mobile accessibility standards.
- Debounce/guard against rapid repeated taps (prevents duplicate navigation).

### 5.4 Local Persistence

- Key: `hasSeenOnboarding` (Boolean).
- Written as `true` **before** navigating to Home Screen, triggered by either Skip **or** Get
  Started.
- State persists across app restarts and force-kill scenarios.
- No network connectivity required to read or write.
- Persistence logic is isolated from UI components (supports unit testing — consistent with the
  clean architecture/MVVM pattern typically used).

### 5.5 Navigation & State Handling

- Onboarding cannot be reopened on a normal launch after completion.
- Back navigation must not create duplicate splash/onboarding screens.
- If the app is terminated during onboarding → `hasSeenOnboarding` **remains false** (onboarding
  incomplete → must be shown again on next launch).
- Home Screen is the final destination after completion or skip.

---

## 6. Acceptance Criteria

**Cold Launch Flow**

- [ ] First launch (`hasSeenOnboarding == false`/unavailable) → Splash displays briefly →
  transitions to Page 1 of the carousel.
- [ ] Subsequent launch (`hasSeenOnboarding == true`) → Splash routes directly to Home Screen.
- [ ] Returning users do not see the carousel again unless the state is intentionally reset.

**Splash Screen**

- [ ] Logo/branding is visible during splash.
- [ ] Local state is read before routing.
- [ ] Splash-to-destination transition is smooth, with no blank/broken screen.

**Slider Navigation**

- [ ] Horizontal swipe works across all three pages.
- [ ] Next button works on Pages 1 & 2.
- [ ] Page indicator updates correctly with the active page.
- [ ] Page 3 displays "Get Started" instead of "Next".
- [ ] Tapping Skip from any page immediately ends onboarding → Home Screen.

**Local State Persistence**

- [ ] Skip → sets `hasSeenOnboarding = true` before navigation.
- [ ] Get Started → sets `hasSeenOnboarding = true` before navigation.
- [ ] Value remains available after restart & force-kill.
- [ ] Onboarding works without network connectivity.

**Mobile Experience**

- [ ] Renders correctly on supported Android & iOS screen sizes.
- [ ] No overlapping or clipped text/controls/indicators/imagery.
- [ ] Interactive elements provide clear visual feedback and meet accessibility requirements.
- [ ] Rapid repeated taps do not trigger duplicate navigation.

**Scope Compliance**

- [ ] All implementation is completed in the mobile client.
- [ ] No backend/API/authentication/server-side changes.

---

## 7. Definition of Done

- Splash Screen & 3-page Welcome Carousel implemented on Android and iOS.
- Skip, Next, and Get Started interactions completed.
- `hasSeenOnboarding` persistence implemented and covered by unit/integration tests.
- Verified scenarios: first-launch, returning-user, skip, completion, restart, force-kill.
- Offline behavior for onboarding state verified.
- Tested on supported physical devices and emulators/simulators.
- Responsive layout and accessibility checks completed.
- Code reviewed and Pull Request approved.
- QA sign-off completed.
- Product & Design sign-off completed.

---

## 8. Risks & Assumptions

| Risk/Assumption                                                                      | Mitigation/Notes                                                                                                                           |
|--------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| Storage mechanism (DataStore/SharedPreferences/MMKV/Async Storage) not yet finalized | Recommendation: DataStore (Android, Google-recommended replacement for SharedPreferences) & AsyncStorage/MMKV (iOS/RN, depending on stack) |
| No official visual mockup/wireframe yet                                              | Design team assets needed before final UI implementation                                                                                   |
| Analytics/event tracking requirements not specified                                  | Clarify with Product whether completion/drop-off needs to be logged                                                                        |
| "Supported screen sizes" not explicitly defined                                      | Device matrix needed from QA/Design                                                                                                        |

---

## 9. Dependencies

- Design team: illustration/imagery assets for the 3 carousel pages, dark-mode specifications.
- QA team: device matrix for Android & iOS testing.
- No backend/API dependency.

---

## 10. Timeline (proposed — to be aligned with sprint planning)

| Phase                       | Estimate |
|-----------------------------|----------|
| Design & asset finalization | TBD      |
| Android implementation      | TBD      |
| iOS implementation          | TBD      |
| Testing & QA                | TBD      |
| Sign-off & release          | TBD      |

---

*Note: This PRD was compiled from an onboarding feature ticket that was already quite detailed at
the engineering level. The Success Metrics, Risks, Dependencies, and Timeline sections were added as
standard PRD scaffolding and need to be validated/filled in with the Product Manager and relevant
teams before finalization.*
