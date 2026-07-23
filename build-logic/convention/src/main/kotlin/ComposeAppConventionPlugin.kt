import ext.BASE_PACKAGE
import ext.apiLibs
import ext.applyPlugins
import ext.featureBasePath
import ext.implementation
import ext.implementationLibs
import ext.kotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

class ComposeAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.applyPlugins("joyvie.kmp.compose")

            val generateKoinModules =
                tasks.register("generateKoinModules", GenerateKoinModulesTask::class.java) {
                    val files = objects.fileCollection()
                    rootProject.subprojects
                        .filter { it.buildFile.exists() }
                        .filter { subproject ->
                            val path = subproject.path
                            path.startsWith(":core:") ||
                                path.endsWith(":presentation") ||
                                path.endsWith(":data") ||
                                path.endsWith(":api")
                        }
                        .forEach { subproject ->
                            val searchDir = subproject.file("src/commonMain/kotlin")
                            if (searchDir.exists()) {
                                files.from(
                                    fileTree(searchDir) {
                                        include("**/di/*.kt")
                                    },
                                )
                            }
                        }
                    sourceFiles.setFrom(files)
                    outputDir.set(layout.buildDirectory.dir("generated/koin/src/commonMain/kotlin"))
                }

            kotlinMultiplatform {
                targets.withType(KotlinNativeTarget::class.java).configureEach {
                    binaries.framework {
                        baseName = "ComposeApp"
                        isStatic = true
                    }
                }

                sourceSets.getByName("commonMain").apply {
                    kotlin.srcDir(generateKoinModules.map { it.outputDir.get() })
                    dependencies {
                        val moduleDependencies =
                            rootProject.subprojects
                                .filter { it.buildFile.exists() }
                                .filter { subproject ->
                                    val path = subproject.path
                                    path.startsWith(":core:") ||
                                        path.endsWith(":presentation") ||
                                        path.endsWith("$featureBasePath:data") ||
                                        path.endsWith(":data") ||
                                        path.endsWith(":api")
                                }
                                .map { project(it.path) }
                                .toTypedArray()

                        implementation(*moduleDependencies)
                        implementationLibs("kotlinx-serialization-json")

                        apiLibs(
                            "navigation3-ui",
                            "androidx-lifecycle-viewmodel-navigation3",
                            "koin-core",
                            "koin-core-viewmodel",
                            "koin-compose",
                        )
                    }
                }
                sourceSets.getByName("commonTest").dependencies {
                    implementationLibs("kotlin-test")
                }
            }

            extensions.configure(ComposeExtension::class.java) {
                extensions.configure(ResourcesExtension::class.java) {
                    packageOfResClass = "${BASE_PACKAGE}.composeapp"
                }
            }
        }
    }
}

abstract class GenerateKoinModulesTask : org.gradle.api.DefaultTask() {
    @get:org.gradle.api.tasks.InputFiles
    @get:org.gradle.api.tasks.PathSensitive(org.gradle.api.tasks.PathSensitivity.RELATIVE)
    abstract val sourceFiles: org.gradle.api.file.ConfigurableFileCollection

    @get:org.gradle.api.tasks.OutputDirectory
    abstract val outputDir: org.gradle.api.file.DirectoryProperty

    @org.gradle.api.tasks.TaskAction
    fun generate() {
        val modules = mutableListOf<Pair<String, String>>()
        sourceFiles.forEach { file ->
            val lines = file.readLines()
            var pkg: String? = null
            val declaredModules = mutableListOf<String>()
            for (line in lines) {
                val pkgMatch = Regex("""^package\s+([\w\.]+)""").find(line)
                if (pkgMatch != null) {
                    pkg = pkgMatch.groupValues[1]
                }
                val moduleMatch = Regex("""val\s+(\w+Module)\s*(:\s*Module)?\s*=""").find(line)
                if (moduleMatch != null) {
                    declaredModules.add(moduleMatch.groupValues[1])
                }
            }
            if (pkg != null && declaredModules.isNotEmpty()) {
                declaredModules.forEach { moduleName ->
                    modules.add(pkg to moduleName)
                }
            }
        }

        val outputFile =
            outputDir.file("com/utaputranto/joyviekmp/di/GeneratedModules.kt").get().asFile
        outputFile.parentFile.mkdirs()

        val imports = modules.joinToString("\n") { (pkg, name) -> "import $pkg.$name" }
        val moduleList = modules.joinToString(",\n    ") { (_, name) -> name }

        outputFile.writeText(
            """
            package com.utaputranto.joyviekmp.di

            import org.koin.core.module.Module
            $imports

            val generatedModules: List<Module> = listOf(
                $moduleList
            )
            """.trimIndent(),
        )
    }
}
