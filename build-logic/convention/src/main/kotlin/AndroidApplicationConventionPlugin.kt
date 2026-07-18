import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Android application module: app identity, SDK levels, Java target, Compose,
 * and build variants from [JoyvieBuildType]. App version lives in the version
 * catalog (app-versionCode / app-versionName).
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<ApplicationExtension> {
                namespace = BASE_PACKAGE
                compileSdk = libsExtension.findVersion("android-compileSdk").get().requiredVersion.toInt()

                defaultConfig {
                    applicationId = BASE_PACKAGE
                    minSdk = libsExtension.findVersion("android-minSdk").get().requiredVersion.toInt()
                    targetSdk = libsExtension.findVersion("android-targetSdk").get().requiredVersion.toInt()
                    versionCode = libsExtension.findVersion("app-versionCode").get().requiredVersion.toInt()
                    versionName = libsExtension.findVersion("app-versionName").get().requiredVersion
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                buildFeatures {
                    compose = true
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }

                buildTypes {
                    debug {
                        applicationIdSuffix = JoyvieBuildType.DEBUG.applicationIdSuffix
                        versionNameSuffix = JoyvieBuildType.DEBUG.versionNameSuffix
                    }
                    release {
                        applicationIdSuffix = JoyvieBuildType.RELEASE.applicationIdSuffix
                        versionNameSuffix = JoyvieBuildType.RELEASE.versionNameSuffix
                        // Keep minify off until R8 rules for kotlinx.serialization
                        // (type-safe navigation routes) are in place and tested.
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro",
                        )
                    }
                }
            }

            dependencies {
                "implementation"(project(":composeApp"))
                "implementation"(project(":core:platform"))
                "implementation"(libsExtension.findLibrary("androidx-activity-compose").get())
                "implementation"(libsExtension.findLibrary("compose-uiToolingPreview").get())
                "debugImplementation"(libsExtension.findLibrary("compose-uiTooling").get())
            }
        }
    }
}
