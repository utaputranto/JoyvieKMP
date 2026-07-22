import ext.apiLibs
import ext.applyPlugins
import ext.implementationLibs
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for feature domain modules (`:feature:<name>:domain`).
 *
 * Applies base KMP library conventions via `joyvie.kmp.library`, adding dependency on `:core:model`,
 * Compose resources, and Koin DI (`koin-core`, `koin-annotations`) for domain use case dependency injection.
 */
class FeatureDomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("joyvie.kmp.library")

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    api(project(":core:model"))
                    apiLibs("compose-components-resources")
                    // Annotated use cases (@Factory) live here.
                    implementationLibs("koin-core", "koin-annotations")
                }
            }
        }
    }
}
