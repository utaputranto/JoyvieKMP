plugins {
    alias(libs.plugins.joyvie.kmp.library)
}

kotlin {
    android {
        namespace = "com.joyvie.core.model"
    }
    // Pure Kotlin Multiplatform domain models, no dependencies needed
}
