import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class SpotlessConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.diffplug.spotless")

            extensions.configure<SpotlessExtension> {
                kotlin {
                    target("**/*.kt")
                    targetExclude("**/build/**/*.kt", "**/build/generated/**/*.kt")
                    ktlint(KTLINT_VERSION)
                }
                kotlinGradle {
                    target("**/*.gradle.kts")
                    targetExclude("**/build/**/*.gradle.kts")
                    ktlint(KTLINT_VERSION)
                }
            }
        }
    }

    private companion object {
        const val KTLINT_VERSION = "1.0.1"
    }
}
