plugins {
    alias(libs.plugins.joyvie.kmp.feature)
}

kotlin {
    android {
        namespace = "com.joyvie.features.auth.presentation"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.auth.domain)
            implementation(projects.core.model)
            implementation(projects.core.designsystem)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
        }
    }
}
