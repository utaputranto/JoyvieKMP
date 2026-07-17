plugins {
    `kotlin-dsl`
}

group = "com.joyvie.buildlogic"

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.multiplatform.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.compose.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("kmpFeature") {
            id = "joyvie.kmp.feature"
            implementationClass = "KmpFeatureConventionPlugin"
        }
        register("kmpLibrary") {
            id = "joyvie.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
    }
}
