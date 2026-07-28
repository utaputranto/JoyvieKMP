plugins {
    alias(libs.plugins.joyvie.feature.presentation)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Cross-feature: navigate back to onboarding
            implementation(projects.feature.onboarding.api)
        }
    }
}

// ViewModel @KoinViewModel deps (use cases) are provided by the domain module; the
// plugin's static check doesn't resolve them across the boundary. Verified at aggregation.
koinCompiler {
    compileSafety = false
}
