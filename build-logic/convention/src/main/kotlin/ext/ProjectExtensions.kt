package ext

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal const val BASE_PACKAGE = "com.utaputranto.joyviekmp"

internal val Project.libsExtension: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal val Project.moduleNamespace: String
    get() = BASE_PACKAGE + path.replace(":", ".")

internal val Project.featureBasePath: String
    get() = path.substringBeforeLast(":")

internal fun Project.kotlinMultiplatform(block: KotlinMultiplatformExtension.() -> Unit) {
    extensions.configure<KotlinMultiplatformExtension>(block)
}

fun Project.libraryVersion(alias: String): String {
    return libsExtension.findVersion(alias)
        .orElseThrow { IllegalArgumentException("Version alias '$alias' not found in catalog") }
        .requiredVersion
}

fun Project.libraryVersionInt(alias: String): Int {
    return libraryVersion(alias).toInt()
}
