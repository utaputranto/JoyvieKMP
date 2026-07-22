package ext

import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderConvertible
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import java.util.Optional

/**
 * Convenience extension for adding `implementation` dependencies in KMP source sets,
 * supporting Providers, Optionals, and standard Gradle notations.
 */
fun KotlinDependencyHandler.implementation(vararg dependencies: Any?) {
    dependencies.filterNotNull().forEach { dependency ->
        when (dependency) {
            is Optional<*> -> dependency.orElse(null)?.let { implementation(it) }
            is Provider<*> -> implementation(dependency.get())
            is ProviderConvertible<*> -> implementation(dependency.asProvider().get())
            else -> implementation(dependency)
        }
    }
}

/**
 * Convenience extension for adding `api` dependencies in KMP source sets,
 * supporting Providers, Optionals, and standard Gradle notations.
 */
fun KotlinDependencyHandler.api(vararg dependencyNotations: Any?) {
    dependencyNotations.filterNotNull().forEach { dependency ->
        when (dependency) {
            is Optional<*> -> dependency.orElse(null)?.let { api(it) }
            is Provider<*> -> api(dependency.get())
            is ProviderConvertible<*> -> api(dependency.asProvider().get())
            else -> api(dependency)
        }
    }
}

/**
 * Adds multiple implementation dependencies referenced by catalog library aliases.
 *
 * @param aliases Variable list of library aliases in the version catalog.
 */
fun KotlinDependencyHandler.implementationLibs(vararg aliases: String) {
    aliases.forEach { alias ->
        project.libsExtension.findLibrary(alias).ifPresent { implementation(it.get()) }
    }
}

/**
 * Adds multiple API dependencies referenced by catalog library aliases.
 *
 * @param aliases Variable list of library aliases in the version catalog.
 */
fun KotlinDependencyHandler.apiLibs(vararg aliases: String) {
    aliases.forEach { alias ->
        project.libsExtension.findLibrary(alias).ifPresent { api(it.get()) }
    }
}
