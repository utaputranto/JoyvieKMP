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
