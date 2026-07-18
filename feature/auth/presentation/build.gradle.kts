plugins {
    alias(libs.plugins.joyvie.feature.presentation)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Cross-feature: navigate back to onboarding
            implementation(projects.feature.onboarding.api)
        }
    }
}
