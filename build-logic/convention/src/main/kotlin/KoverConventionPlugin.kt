import ext.applyPlugins
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for JetBrains Kover Code Coverage (`joyvie.kover`).
 *
 * Configures JetBrains Kover 0.9.x+ to aggregate code coverage across KMP targets
 * (commonMain, androidMain, iosMain), applies exclusions for non-testable code (Compose UI,
 * generated code, DI modules), and auto-generates HTML and XML reports on `check` / `test`.
 */
class KoverConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("org.jetbrains.kotlinx.kover")

            extensions.configure(KoverProjectExtension::class.java) {
                reports {
                    filters {
                        excludes {
                            // Exclude Jetpack Compose UI functions
                            annotatedBy("androidx.compose.runtime.Composable")

                            // Exclude Generated code & Platform Target Classes (_android, _ios, $, etc.)
                            classes(
                                "*$*",
                                "*_android*",
                                "*_ios*",
                                "*BuildConfig",
                                "*ModuleKt",
                                "*.di.*",
                                "*Generated*",
                                "*GeneratedModules*",
                                "*.generated.resources.*",
                            )
                        }
                    }

                    total {
                        html {
                            onCheck.set(true)
                        }
                        xml {
                            onCheck.set(true)
                        }
                    }
                }
            }
        }
    }
}
