import ext.api
import ext.apiLibs
import ext.applyPlugins
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("joyvie.kmp.compose")

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    api(project(":core:designsystem"))
                    apiLibs("compose-components-resources")
                }
            }
        }
    }
}
