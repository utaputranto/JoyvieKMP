import ext.apiLibs
import ext.applyPlugins
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for Kotlin Multiplatform Compose Feature modules.
 *
 * Extends [ComposeConventionPlugin] (via `joyvie.kmp.compose`) with standard feature dependencies
 * including `:core:designsystem` and Compose Multiplatform resource components (`compose-components-resources`).
 * Also automatically inherits Dokka V2 documentation generation via Compose library conventions.
 */
class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("joyvie.kmp.compose")

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    api(project(":core:designsystem"))
                    apiLibs("compose-components-resources")
                }

                sourceSets.getByName("commonTest").dependencies {
                    implementation(project(":core:test"))
                }
            }
        }
    }
}
