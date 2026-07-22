import ext.apiLibs
import ext.applyPlugins
import ext.implementationLibs
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for feature API modules (`:feature:<name>:api`).
 *
 * Configures base KMP library capabilities via `joyvie.kmp.library`, adding Navigation 3 runtime
 * dependencies (`navigation3-runtime`) for declaring navigation route keys (`NavKey`) and route extensions.
 *
 * Feature API modules allow cross-feature navigation without exposing implementation details.
 */
class FeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("joyvie.kmp.library")

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    apiLibs("navigation3-runtime")
                    implementationLibs("kotlinx-serialization-json")
                }
            }
        }
    }
}
