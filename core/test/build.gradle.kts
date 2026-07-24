plugins {
    alias(libs.plugins.joyvie.kmp.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)
            implementation(projects.core.datastore)
            implementation(projects.feature.onboarding.domain)

            api(libs.kotest.framework.engine)
            api(libs.kotest.assertions.core)
            api(libs.koin.test)
            api(libs.kotlinx.coroutines.test)
            api(libs.turbine)
        }

        androidMain.dependencies {
            api(libs.kotest.runner.junit5)
            api(libs.junit.platform.launcher)
        }
    }
}
