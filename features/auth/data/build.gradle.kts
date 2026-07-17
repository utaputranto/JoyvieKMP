plugins {
    alias(libs.plugins.joyvie.kmp.library)
}

kotlin {
    android {
        namespace = "com.joyvie.features.auth.data"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.auth.domain)
            implementation(projects.core.model)
            implementation(projects.core.network)
        }
    }
}
