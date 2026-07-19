import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.sonarqube.gradle.SonarExtension

class SonarConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.sonarqube")

            extensions.configure<SonarExtension> {
                properties {
                    property("sonar.projectKey", System.getenv("SONAR_PROJECT_KEY").orEmpty())
                    property("sonar.organization", System.getenv("SONAR_ORGANIZATION").orEmpty())
                    property("sonar.host.url", System.getenv("SONAR_HOST_URL"))
                    property("sonar.token", System.getenv("SONAR_TOKEN"))
                    property("sonar.sourceEncoding", "UTF-8")
                    property("sonar.gradle.skipCompile", "true")
                    property("sonar.exclusions", EXCLUSIONS.joinToString(","))
                    property("sonar.coverage.exclusions", "**/*")
                }
            }

            subprojects {
                extensions.configure<SonarExtension> {
                    properties {
                        // Android application modules are auto-detected by the
                        // Sonar plugin via AGP; configuring them manually would
                        // index the same files twice.
                        if (!pluginManager.hasPlugin("com.android.application")) {
                            val sourceDirs = SOURCE_DIRS.filter { file(it).exists() }
                            val testDirs = TEST_DIRS.filter { file(it).exists() }

                            if (sourceDirs.isNotEmpty()) {
                                property("sonar.sources", sourceDirs.joinToString(","))
                            }
                            if (testDirs.isNotEmpty()) {
                                property("sonar.tests", testDirs.joinToString(","))
                            }
                        }
                    }
                }
            }
        }
    }

    private companion object {
        val EXCLUSIONS =
            listOf(
                "**/build/**",
                "**/generated/**",
                "iosApp/**",
            )

        val SOURCE_DIRS =
            listOf(
                "src/commonMain/kotlin",
                "src/androidMain/kotlin",
                "src/iosMain/kotlin",
                "src/main/kotlin",
            )

        val TEST_DIRS =
            listOf(
                "src/commonTest/kotlin",
                "src/androidUnitTest/kotlin",
                "src/iosTest/kotlin",
                "src/test/kotlin",
            )
    }
}
