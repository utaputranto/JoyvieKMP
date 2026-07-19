import java.util.Properties

plugins {
    alias(libs.plugins.joyvie.kmp.library)
}

abstract class GenerateBuildConfigTask : DefaultTask() {
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Input
    @get:Optional
    abstract val tmdbToken: Property<String>

    @get:InputFile
    @get:Optional
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val localPropertiesFile: RegularFileProperty

    @TaskAction
    fun generate() {
        var token = tmdbToken.orNull.orEmpty()

        if (token.isEmpty() && localPropertiesFile.isPresent) {
            val file = localPropertiesFile.get().asFile
            if (file.exists()) {
                val properties = Properties()
                file.inputStream().use { properties.load(it) }
                token = properties.getProperty("tmdb.token").orEmpty()
            }
        }

        val file = outputDir.file("com/utaputranto/joyviekmp/core/network/BuildConfig.kt").get().asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            package com.utaputranto.joyviekmp.core.network

            object BuildConfig {
                const val TMDB_TOKEN = "$token"
            }
            """.trimIndent(),
        )
    }
}

val generateBuildConfig =
    tasks.register<GenerateBuildConfigTask>("generateBuildConfig") {
        outputDir.set(layout.buildDirectory.dir("generated/source/buildConfig/commonMain/kotlin"))
        tmdbToken.set(providers.environmentVariable("TMDB_TOKEN").orElse(""))
        localPropertiesFile.set(project.rootProject.layout.projectDirectory.file("local.properties"))
    }

kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir(generateBuildConfig)
            dependencies {
                implementation(projects.core.model)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.koin.core)
            }
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}
