import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * Base KMP library module: Android + iOS targets, serialization plugin.
 * The Android namespace is inferred from the module path
 * (":core:model" -> "com.utaputranto.joyviekmp.core.model").
 */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            kotlinMultiplatform {
                val androidTarget = targets.getByName("android") as KotlinMultiplatformAndroidLibraryTarget
                androidTarget.apply {
                    namespace = moduleNamespace.lowercase()
                    compileSdk = libsExtension.findVersion("android-compileSdk").get().requiredVersion.toInt()
                    minSdk = libsExtension.findVersion("android-minSdk").get().requiredVersion.toInt()

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
