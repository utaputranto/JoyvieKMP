import ext.applyPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for Compose Multiplatform library modules.
 *
 * Combines [KmpLibraryConventionPlugin] (`joyvie.kmp.library`), the JetBrains Compose plugin (`org.jetbrains.compose`),
 * and the Kotlin Compose Compiler plugin (`org.jetbrains.kotlin.plugin.compose`).
 *
 * Provides shared Compose UI setup, compiler plugin configuration, and inherits Dokka V2 documentation generation.
 */
class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins(
                "joyvie.kmp.library",
                "org.jetbrains.compose",
                "org.jetbrains.kotlin.plugin.compose",
            )
        }
    }
}
