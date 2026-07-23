import ext.applyPlugins
import ext.featureBasePath
import ext.implementation
import ext.implementationLibs
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project

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
