package ext

import org.gradle.api.plugins.PluginManager

/**
 * Applies multiple Gradle plugins by plugin ID.
 *
 * @param pluginIds Variable list of plugin IDs to apply.
 */
fun PluginManager.applyPlugins(vararg pluginIds: String) {
    pluginIds.forEach { apply(it) }
}
