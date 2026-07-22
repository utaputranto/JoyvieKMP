plugins {
    alias(libs.plugins.joyvie.kmp.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            api(libs.compose.material3)
            api(libs.compose.ui)
            api(libs.compose.components.resources)
            api(libs.compose.uiToolingPreview)

            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor3)
        }
        androidMain.dependencies {
            api(libs.compose.uiTooling)
        }
    }
}
