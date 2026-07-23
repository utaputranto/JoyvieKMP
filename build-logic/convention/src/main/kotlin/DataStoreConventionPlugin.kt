import ext.apiLibs
import ext.applyPlugins
import ext.implementationLibs
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

class DataStoreConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("joyvie.kmp.library")

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    apiLibs("kotlinx-coroutines-core")
                    implementationLibs(
                        "androidx-datastore-preferences-core",
                        "androidx-datastore-core-okio",
                        "koin-core",
                    )
                }
            }
        }
    }
}
