import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply necessary Gradle plugins
            pluginManager.apply("org.jetbrains.kotlin.multiplatform")
            pluginManager.apply("com.android.kotlin.multiplatform.library")
            pluginManager.apply("org.jetbrains.compose")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            // Configure Kotlin Multiplatform
            extensions.configure<KotlinMultiplatformExtension> {
                // Setup Android Target using android multiplatform library DSL
                val androidTarget = targets.getByName("android") as KotlinMultiplatformAndroidLibraryTarget
                androidTarget.apply {
                    val compileSdkVal = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
                    val minSdkVal = libs.findVersion("android-minSdk").get().requiredVersion.toInt()

                    compileSdk = compileSdkVal
                    minSdk = minSdkVal

                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_17)
                    }

                    androidResources {
                        enable = true
                    }
                }

                // Setup iOS Targets
                iosArm64()
                iosSimulatorArm64()

                // Configure Source Sets
                sourceSets.apply {
                    val commonMain = findByName("commonMain") ?: create("commonMain")

                    commonMain.dependencies {
                        implementation(libs.findLibrary("compose-runtime").get())
                        implementation(libs.findLibrary("compose-foundation").get())
                        implementation(libs.findLibrary("compose-material3").get())
                        implementation(libs.findLibrary("compose-ui").get())
                        implementation(libs.findLibrary("compose-components-resources").get())
                        implementation(libs.findLibrary("compose-uiToolingPreview").get())
                    }
                }
            }
        }
    }
}
