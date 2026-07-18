plugins {
    alias(libs.plugins.joyvie.feature.presentation)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Cross-feature: navigate forward to auth
            implementation(projects.feature.auth.api)
        }
    }
}
