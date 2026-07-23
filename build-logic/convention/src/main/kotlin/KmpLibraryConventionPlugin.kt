import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import ext.applyPlugins
import ext.kotlinMultiplatform
import ext.libraryVersionInt
import ext.moduleNamespace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins(
                "org.jetbrains.kotlin.multiplatform",
                "com.android.kotlin.multiplatform.library",
                "org.jetbrains.kotlin.plugin.serialization",
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
