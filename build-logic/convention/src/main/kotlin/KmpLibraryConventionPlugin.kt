import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import ext.applyPlugins
import ext.kotlinMultiplatform
import ext.libraryVersionInt
import ext.moduleNamespace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * Convention plugin for Kotlin Multiplatform (KMP) Library modules.
 *
 * Configures base KMP targets (Android library target + iOS targets `iosArm64` and `iosSimulatorArm64`),
 * Kotlin Serialization, automatic namespace derivation from the Gradle module path, and Dokka V2 documentation.
 *
 * Applicable to core non-UI modules (e.g. `:core:model`, `:core:network`, `:core:platform`, `:core:datastore`)
 * as well as feature domain, data, and API modules.
 */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins(
                "org.jetbrains.kotlin.multiplatform",
                "com.android.kotlin.multiplatform.library",
                "org.jetbrains.kotlin.plugin.serialization",
                "joyvie.dokka",
            )

            kotlinMultiplatform {
                val androidTarget = targets.getByName("android") as KotlinMultiplatformAndroidLibraryTarget
                androidTarget.apply {
                    namespace = moduleNamespace.lowercase()
                    compileSdk = libraryVersionInt("android-compileSdk")
                    minSdk = libraryVersionInt("android-minSdk")

                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_17)
                    }

                    androidResources {
                        enable = true
                    }
                }

                iosArm64()
                iosSimulatorArm64()
            }
        }
    }
}
