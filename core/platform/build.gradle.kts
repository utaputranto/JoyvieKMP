plugins {
    alias(libs.plugins.joyvie.kmp.library)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.koin.core)
            }
        }
    }
}
