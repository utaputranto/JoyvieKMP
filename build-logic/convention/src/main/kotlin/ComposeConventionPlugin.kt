import ext.applyPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project

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
