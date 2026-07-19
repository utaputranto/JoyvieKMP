import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

/**
 * App aggregator module (composeApp): Compose entry point + iOS framework.
 *
 * - Static iOS framework "ComposeApp" for both targets.
 * - Auto-depends on ALL :feature:* and :core:* modules, so a new feature is
 *   wired into the app without touching this build file.
 * - App-level dependencies: navigation, serialization, Koin.
 */
class ComposeAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("joyvie.kmp.feature")

            kotlinMultiplatform {
                targets.withType(KotlinNativeTarget::class.java).configureEach {
                    binaries.framework {
                        baseName = "ComposeApp"
                        isStatic = true
                    }
                }

                sourceSets.getByName("commonMain").dependencies {
                    rootProject.subprojects
                        .filter { it.buildFile.exists() }
                        .filter { subproject ->
                            val path = subproject.path
                            path.startsWith(":core:") ||
                                path.endsWith(":presentation") ||
                                path.endsWith(":data") ||
                                path.endsWith(":api")
                        }
                        .forEach { implementation(project(it.path)) }

                    implementation(libsExtension.findLibrary("androidx-navigation-compose").get())
                    implementation(libsExtension.findLibrary("kotlinx-serialization-json").get())
                    api(libsExtension.findLibrary("koin-core").get())
                    implementation(libsExtension.findLibrary("koin-core-viewmodel").get())
                    implementation(libsExtension.findLibrary("koin-compose").get())
                }
                sourceSets.getByName("commonTest").dependencies {
                    implementation(libsExtension.findLibrary("kotlin-test").get())
                }
            }

            extensions.configure(ComposeExtension::class.java) {
                extensions.configure(ResourcesExtension::class.java) {
                    packageOfResClass = "$BASE_PACKAGE.composeapp"
                }
            }
        }
    }
}
