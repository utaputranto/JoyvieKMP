import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Feature `domain` layer: repository contracts + use cases.
 */
class FeatureDomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("joyvie.kmp.library")

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    api(project(":core:model"))
                    api(libsExtension.findLibrary("compose-components-resources").get())
                }
            }
        }
    }
}
