plugins {
    alias(libs.plugins.joyvie.kmp.feature)
}

kotlin {
    android {
        namespace = "com.utaputranto.joyviekmp.shared"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Dependencies on core and feature modules
            implementation(projects.core.designsystem)
            implementation(projects.core.network)
            implementation(projects.core.model)
            implementation(projects.features.auth.presentation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

compose.resources {
    packageOfResClass = "com.utaputranto.joyviekmp.shared"
}
