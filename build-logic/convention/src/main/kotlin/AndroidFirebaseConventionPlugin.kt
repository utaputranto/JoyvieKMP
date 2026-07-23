import ext.applyPlugins
import ext.libsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("com.google.gms.google-services")

            dependencies {
                val bom = libsExtension.findLibrary("firebase-bom").get()
                val analytics = libsExtension.findLibrary("firebase-analytics").get()

                "implementation"(platform(bom))
                "implementation"(analytics)
            }
        }
    }
}
