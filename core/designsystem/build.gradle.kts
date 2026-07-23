plugins {
    alias(libs.plugins.joyvie.kmp.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Compose UI stack — re-exported (api) so any module depending on
            // :core:designsystem (feature `presentation`, the app) gets it transitively.
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            api(libs.compose.material3)
            api(libs.compose.ui)
            api(libs.compose.components.resources)
            api(libs.compose.uiToolingPreview)

            // Image loading is an internal detail of JoyviePosterCard — not part of the
            // public API, so it stays implementation and never leaks to consumers.
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor3)
        }
        androidMain.dependencies {
            // @Preview renderer used by Android Studio; needed by consumers previewing
            // design-system components, hence api.
            api(libs.compose.uiTooling)
        }
    }
}
