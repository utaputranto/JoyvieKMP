import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Feature `presentation` layer: Compose screens + ViewModel + nav graph.
 * Automatically depends on the sibling `api` and `domain` modules of the same
 * feature. Cross-feature dependencies (e.g. another feature's api) stay
 * declared in each module's own build.gradle.kts.
 */
class FeaturePresentationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("joyvie.kmp.feature")

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    implementation(project("$featureBasePath:api"))
                    implementation(project("$featureBasePath:domain"))
                    implementation(project(":core:model"))
                    implementation(project(":core:platform"))
                    api(project(":core:designsystem"))
                    implementation(libsExtension.findLibrary("androidx-lifecycle-viewmodelCompose").get())
                    implementation(libsExtension.findLibrary("androidx-navigation-compose").get())
                    implementation(libsExtension.findLibrary("koin-core").get())
                    implementation(libsExtension.findLibrary("koin-core-viewmodel").get())
                    implementation(libsExtension.findLibrary("koin-compose-viewmodel").get())
                    implementation(libsExtension.findLibrary("compose-navigationevent").get())
                }
            }
        }
    }
}
