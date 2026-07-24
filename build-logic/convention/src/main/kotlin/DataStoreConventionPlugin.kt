import ext.apiLibs
import ext.applyPlugins
import ext.implementationLibs
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for DataStore persistence library modules (`joyvie.kmp.datastore`).
 *
 * Configures base KMP library setup via `joyvie.kmp.library`, adding dependencies for kotlinx-coroutines,
 * Jetpack DataStore Preferences Core, DataStore Okio core, and Koin DI.
 */
class DataStoreConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("joyvie.kmp.library")

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    apiLibs(
                        "kotlinx-coroutines-core",
                        "androidx-datastore-preferences-core",
                        "androidx-datastore-core-okio",
                        "kotlinx-serialization-json",
                    )
                    implementationLibs(
                        "koin-core",
                        "koin-annotations",
                    )
                }
            }
        }
    }
}
