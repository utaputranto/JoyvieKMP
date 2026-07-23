plugins {
    `kotlin-dsl`
}

group = "com.joyvie.buildlogic"

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.multiplatform.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.kotlin.serialization.plugin)
    compileOnly(libs.compose.gradle.plugin)
    implementation(libs.spotless.gradle.plugin)
    implementation(libs.sonarqube.gradle.plugin)
    implementation(libs.google.services.gradle.plugin)
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
        register("kmpCompose") {
            id = "joyvie.kmp.compose"
            implementationClass = "ComposeConventionPlugin"
        }
        register("kmpDataStore") {
            id = "joyvie.kmp.datastore"
            implementationClass = "DataStoreConventionPlugin"
        }
        register("spotless") {
            id = "joyvie.spotless"
            implementationClass = "SpotlessConventionPlugin"
        }
        register("sonar") {
            id = "joyvie.sonar"
            implementationClass = "SonarConventionPlugin"
        }
        register("featureApi") {
            id = "joyvie.feature.api"
            implementationClass = "FeatureApiConventionPlugin"
        }
        register("featureDomain") {
            id = "joyvie.feature.domain"
            implementationClass = "FeatureDomainConventionPlugin"
        }
        register("featureData") {
            id = "joyvie.feature.data"
            implementationClass = "FeatureDataConventionPlugin"
        }
        register("androidApplication") {
            id = "joyvie.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("kmpApplication") {
            id = "joyvie.kmp.application"
            implementationClass = "ComposeAppConventionPlugin"
        }
        register("featurePresentation") {
            id = "joyvie.feature.presentation"
            implementationClass = "FeaturePresentationConventionPlugin"
        }
        register("androidFirebase") {
            id = "joyvie.android.firebase"
            implementationClass = "AndroidFirebaseConventionPlugin"
        }
    }
}
