import ext.applyPlugins
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for JetBrains Kover Code Coverage (`joyvie.kover`).
 *
 * Configures JetBrains Kover 0.9.x+ across JoyvieKMP modules (:core:*, :feature:*, :composeApp).
 * Applies includes/excludes filters for testable code, auto-generates HTML/XML reports,
 * and configures merged aggregate verification rules at root project level.
 */
class KoverConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            configureKover()
        }
}

private fun Project.configureKover() {
    pluginManager.applyPlugins("org.jetbrains.kotlinx.kover")

    extensions.configure<KoverProjectExtension> {
        reports {
            filters {
                includes {
                    classes(
                        "*StateMachine",
                        "*UseCase",
                        "*RepositoryImpl",
                        "*DataSourceImpl",
                        "*Mapper*",
                        "*ViewModel",
                        "*ExtensionsKt",
                        "*FactoryKt",
                    )
                }
                excludes {
                    annotatedBy("androidx.compose.runtime.Composable")
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
            if (project == rootProject) {
                verify {
                    rule {
                        bound {
                            minValue.set(80)
                            coverageUnits.set(CoverageUnit.LINE)
                            aggregationForGroup.set(AggregationType.COVERED_PERCENTAGE)
                        }
                        bound {
                            minValue.set(30)
                            coverageUnits.set(CoverageUnit.BRANCH)
                            aggregationForGroup.set(AggregationType.COVERED_PERCENTAGE)
                        }
                    }
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
        if (project == rootProject) {
            merge {
                subprojects { proj ->
                    listOf(
                        ":core:",
                        ":feature:",
                        ":composeApp",
                    ).any { proj.path.startsWith(it) }
                }
            }
        }
    }
}
