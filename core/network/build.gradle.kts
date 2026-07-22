import java.util.Properties

plugins {
    alias(libs.plugins.joyvie.kmp.library)
    alias(libs.plugins.koin.compiler)
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
        val localProperties = project.rootProject.layout.projectDirectory.file("local.properties")
        if (localProperties.asFile.exists()) {
            localPropertiesFile.set(localProperties)
        }
    }

kotlin {
    sourceSets {
        commonMain {
            kotlin.srcDir(generateBuildConfig)
            dependencies {
                api(projects.core.platform)
                api(projects.core.model)
                api(libs.ktor.client.core)
                api(libs.ktor.client.content.negotiation)
                api(libs.ktor.serialization.kotlinx.json)
                api(libs.koin.core)
                implementation(libs.koin.annotations)
            }
        }
        androidMain.dependencies {
            api(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            api(libs.ktor.client.darwin)
        }
    }
}
