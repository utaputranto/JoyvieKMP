plugins {
    alias(libs.plugins.joyvie.feature.data)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.components.resources)
        }
    }
}

// Repos depend on providers in core:network / core:datastore; the plugin's static check
// doesn't resolve those across module boundaries. Verified at app-level aggregation.
koinCompiler {
    compileSafety = false
}
