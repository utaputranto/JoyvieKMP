import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * KMP library module with Compose UI: everything from [KmpLibraryConventionPlugin]
 * plus the Compose plugins and common Compose dependencies.
 */
class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("joyvie.kmp.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            kotlinMultiplatform {
                sourceSets.getByName("commonMain").dependencies {
                    api(libsExtension.findLibrary("compose-runtime").get())
                    api(libsExtension.findLibrary("compose-foundation").get())
                    api(libsExtension.findLibrary("compose-material3").get())
                    api(libsExtension.findLibrary("compose-ui").get())
                    api(libsExtension.findLibrary("compose-components-resources").get())
                    api(libsExtension.findLibrary("compose-uiToolingPreview").get())
                    implementation(libsExtension.findLibrary("coil3-compose").get())
                    implementation(libsExtension.findLibrary("coil3-network-ktor3").get())
                }
                // Renderer used by Android Studio to display @Preview composables;
                // without it previews in this module never render.
                sourceSets.getByName("androidMain").dependencies {
                    api(libsExtension.findLibrary("compose-uiTooling").get())
                }
            }
        }
    }
}
