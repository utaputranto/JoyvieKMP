plugins {
    alias(libs.plugins.joyvie.kmp.library)
}

kotlin {
    android {
        namespace = "com.joyvie.features.auth.domain"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.model)
        }
    }
}
