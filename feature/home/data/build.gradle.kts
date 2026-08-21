plugins {
    alias(libs.plugins.joyvie.feature.data)
    alias(libs.plugins.koin.compiler)
}

// Repo depends on the HttpClient provider in core:network; the plugin's static check
// doesn't resolve it across module boundaries. Verified at app-level aggregation.
koinCompiler {
    compileSafety = false
}
