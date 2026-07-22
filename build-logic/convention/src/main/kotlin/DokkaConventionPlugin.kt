import ext.applyPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin that applies Dokka V2 (v2.2.0) for Kotlin Multiplatform documentation generation.
 *
 * Automatically applies the `org.jetbrains.dokka` Gradle plugin to the target project, enabling
 * standardized HTML documentation generation tasks (`dokkaGenerate`, `dokkaGeneratePublicationHtml`, etc.)
 * across all subprojects in the repository.
 *
 * @see <a href="https://kotlinlang.org/docs/dokka-migration.html">Dokka V2 Documentation</a>
 */
class DokkaConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("org.jetbrains.dokka")
        }
    }
}
