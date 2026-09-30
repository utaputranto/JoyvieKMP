import ext.applyPlugins
import ext.featureBasePath
import ext.implementation
import ext.implementationLibs
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for feature data modules (`:feature:<name>:data`).
 *
 * Applies base KMP library conventions via `joyvie.kmp.library`, automatically configuring dependencies
 * on sibling domain module (`:domain`), `:core:model`, `:core:network`, `:core:datastore`, and Koin DI.
 */
class FeatureDataConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("joyvie.kmp.library")

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    implementation(
                        project("$featureBasePath:domain"),
                        project(":core:model"),
                        project(":core:network"),
                        project(":core:datastore"),
                    )
                    implementationLibs("koin-core", "koin-annotations")
                }
            }
        }
    }
}
