package ext

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Base package name for the application and modules.
 */
internal const val BASE_PACKAGE = "com.utaputranto.joyviekmp"

/**
 * Convenience getter to access the root `libs` version catalog.
 */
internal val Project.libsExtension: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

/**
 * Automatically derives package namespace from the Gradle project path.
 *
 * Example: `:core:model` -> `com.utaputranto.joyviekmp.core.model`
 */
internal val Project.moduleNamespace: String
    get() = BASE_PACKAGE + path.replace(":", ".")

/**
 * Helper to extract the feature base path from subproject modules.
 *
 * Example: `:feature:onboarding:domain` -> `:feature:onboarding`
 */
internal val Project.featureBasePath: String
    get() = path.substringBeforeLast(":")

/**
 * Type-safe configuration block for [KotlinMultiplatformExtension].
 */
internal fun Project.kotlinMultiplatform(block: KotlinMultiplatformExtension.() -> Unit) {
    extensions.configure<KotlinMultiplatformExtension>(block)
}

/**
 * Resolves a version string from the `libs` version catalog by version alias.
 *
 * @param alias Version reference key in catalog (e.g. `android-compileSdk`)
 * @return The required version string.
 */
fun Project.libraryVersion(alias: String): String {
    return libsExtension.findVersion(alias)
        .orElseThrow { IllegalArgumentException("Version alias '$alias' not found in catalog") }
        .requiredVersion
}

/**
 * Resolves an integer version number from the `libs` version catalog.
 *
 * @param alias Version reference key in catalog (e.g. `android-minSdk`)
 * @return The version parsed as integer.
 */
fun Project.libraryVersionInt(alias: String): Int {
    return libraryVersion(alias).toInt()
}
