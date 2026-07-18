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
                    implementation(libsExtension.findLibrary("compose-runtime").get())
                    implementation(libsExtension.findLibrary("compose-foundation").get())
                    implementation(libsExtension.findLibrary("compose-material3").get())
                    implementation(libsExtension.findLibrary("compose-ui").get())
                    implementation(libsExtension.findLibrary("compose-components-resources").get())
                    implementation(libsExtension.findLibrary("compose-uiToolingPreview").get())
                }
                // Renderer used by Android Studio to display @Preview composables;
                // without it previews in this module never render.
                sourceSets.getByName("androidMain").dependencies {
                    implementation(libsExtension.findLibrary("compose-uiTooling").get())
                }
            }
        }
    }
}
