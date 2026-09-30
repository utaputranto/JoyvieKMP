import ext.implementation

plugins {
    alias(libs.plugins.joyvie.kmp.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(
                libs.androidx.lifecycle.viewmodel,
                libs.androidx.lifecycle.viewmodelCompose,
                libs.kotlinx.coroutines.core,
                libs.koin.compose,
                libs.koin.compose.viewmodel,
            )
        }
    }
}
