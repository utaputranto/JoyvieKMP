import ext.applyPlugins
import ext.featureBasePath
import ext.implementation
import ext.implementationLibs
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for feature presentation modules (`:feature:<name>:presentation`).
 *
 * Applies `joyvie.kmp.feature` Compose conventions, automatically adding dependencies to sibling
 * `api` and `domain` modules, `:core:model`, `:core:platform`, ViewModel lifecycle extensions,
 * Navigation 3 runtime, and Koin ViewModel integration.
 */
class FeaturePresentationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("joyvie.kmp.feature")

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    implementation(
                        project("$featureBasePath:api"),
                        project("$featureBasePath:domain"),
                        project(":core:model"),
                        project(":core:platform"),
                    )
                    implementationLibs(
                        "androidx-lifecycle-viewmodelCompose",
                        "navigation3-runtime",
                        "koin-core",
                        "koin-core-viewmodel",
                        "koin-compose-viewmodel",
                        "koin-annotations",
                        "compose-navigationevent",
                    )
                }
            }
        }
    }
}
