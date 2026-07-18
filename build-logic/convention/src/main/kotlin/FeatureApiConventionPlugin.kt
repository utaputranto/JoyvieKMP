import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Feature `api` layer: route definitions + navigation contract.
 * The Android namespace is inferred from the module path.
 */
class FeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("joyvie.kmp.library")

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    implementation(libsExtension.findLibrary("androidx-navigation-compose").get())
                    implementation(libsExtension.findLibrary("kotlinx-serialization-json").get())
                }
            }
        }
    }
}
