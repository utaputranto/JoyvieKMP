import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Feature `data` layer: repository implementations.
 * Automatically depends on the sibling `domain` module of the same feature.
 */
class FeatureDataConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("joyvie.kmp.library")

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    implementation(project("$featureBasePath:domain"))
                    implementation(project(":core:model"))
                    implementation(project(":core:network"))
                    implementation(libsExtension.findLibrary("koin-core").get())
                }
            }
        }
    }
}
