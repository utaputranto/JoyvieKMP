import ext.apiLibs
import ext.applyPlugins
import ext.implementationLibs
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

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
