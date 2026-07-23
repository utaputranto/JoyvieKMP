import ext.BASE_PACKAGE
import ext.apiLibs
import ext.applyPlugins
import ext.featureBasePath
import ext.implementation
import ext.implementationLibs
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

/**
 * Convention plugin for `composeApp` multiplatform root entry application.
 *
 * Applies Compose conventions via `joyvie.kmp.compose`, configures iOS framework binaries (`ComposeApp.framework`),
 * automatically scans and wires dependencies for all `:core:*` and `:feature:*` modules in `rootProject.subprojects`,
 * configures Compose resources package names, and inherits Dokka V2 documentation generation.
 */
class ComposeAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("joyvie.kmp.compose")

            kotlinMultiplatform {
                targets.withType(KotlinNativeTarget::class.java).configureEach {
                    binaries.framework {
                        baseName = "ComposeApp"
                        isStatic = true
                    }
                }

                sourceSets.getByName("commonMain").apply {
                    dependencies {
                        val moduleDependencies =
                            rootProject.subprojects
                                .filter { it.buildFile.exists() }
                                .filter { subproject ->
                                    val path = subproject.path
                                    path.startsWith(":core:") ||
                                        path.endsWith(":presentation") ||
                                        path.endsWith("$featureBasePath:data") ||
                                        path.endsWith(":data") ||
                                        path.endsWith(":domain") ||
                                        path.endsWith(":api")
                                }
                                .map { project(it.path) }
                                .toTypedArray()

                        implementation(*moduleDependencies)
                        implementationLibs("kotlinx-serialization-json")

                        apiLibs(
                            "navigation3-ui",
                            "androidx-lifecycle-viewmodel-navigation3",
                            "koin-core",
                            "koin-core-viewmodel",
                            "koin-compose",
                            "koin-annotations",
                        )
                    }
                }
                sourceSets.getByName("commonTest").dependencies {
                    implementationLibs("kotlin-test")
                }
            }

            extensions.configure(ComposeExtension::class.java) {
                extensions.configure(ResourcesExtension::class.java) {
                    packageOfResClass = "${BASE_PACKAGE}.composeapp"
                }
            }
        }
    }
}
