package com.utaputranto.joyviekmp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

val defaultDataStoreJson =
    Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

/**
 * Reads a serialized object of type [T] from [DataStore] for a given [key] using default Json.
 * Returns a [Flow] emitting null if the key doesn't exist or deserialization fails.
 */
inline fun <reified T> DataStore<Preferences>.getObject(key: Preferences.Key<String>): Flow<T?> = getObject(key, defaultDataStoreJson)

/**
 * Reads a serialized object of type [T] from [DataStore] for a given [key] using custom [json].
 * Returns a [Flow] emitting null if the key doesn't exist or deserialization fails.
 */
inline fun <reified T> DataStore<Preferences>.getObject(
    key: Preferences.Key<String>,
    json: Json,
): Flow<T?> {
    return data.map { preferences ->
        preferences.getObject<T>(key, json)
    }
}

/**
 * Reads a serialized object of type [T] from [Preferences] for a given [key] using default Json.
 * Returns null if the key doesn't exist or deserialization fails.
 */
inline fun <reified T> Preferences.getObject(key: Preferences.Key<String>): T? = getObject(key, defaultDataStoreJson)

/**
 * Reads a serialized object of type [T] from [Preferences] for a given [key] using custom [json].
 * Returns null if the key doesn't exist or deserialization fails.
 */
inline fun <reified T> Preferences.getObject(
    key: Preferences.Key<String>,
    json: Json,
): T? {
    val jsonString = this[key] ?: return null
    return try {
        json.decodeFromString<T>(jsonString)
    } catch (_: Exception) {
        null
    }
}

/**
 * Saves a serialized object of type [T] into [DataStore] under [key] using default Json.
 */
suspend inline fun <reified T> DataStore<Preferences>.putObject(
    key: Preferences.Key<String>,
    value: T,
): Unit = putObject(key, value, defaultDataStoreJson)

/**
 * Saves a serialized object of type [T] into [DataStore] under [key] using custom [json].
 */
suspend inline fun <reified T> DataStore<Preferences>.putObject(
    key: Preferences.Key<String>,
    value: T,
    json: Json,
) {
    val jsonString = json.encodeToString(serializer<T>(), value)
    edit { preferences ->
        preferences[key] = jsonString
    }
}

/**
 * Removes an object entry from [DataStore] for a given [key].
 */
suspend fun DataStore<Preferences>.removeObject(key: Preferences.Key<*>) {
    edit { preferences ->
        preferences.remove(key)
    }
}

/**
 * Reads a serialized object of type [T] from [DataStoreManager] for a given [key] using default Json.
 */
inline fun <reified T> DataStoreManager.getObject(key: Preferences.Key<String>): Flow<T?> = getObject(key, defaultDataStoreJson)

/**
 * Reads a serialized object of type [T] from [DataStoreManager] for a given [key] using custom [json].
 */
inline fun <reified T> DataStoreManager.getObject(
    key: Preferences.Key<String>,
    json: Json,
): Flow<T?> {
    return getOrNull(key).map { jsonString ->
        jsonString?.let {
            try {
                json.decodeFromString<T>(it)
            } catch (_: Exception) {
                null
            }
        }
    }
}

/**
 * Saves a serialized object of type [T] into [DataStoreManager] under [key] using default Json.
 */
suspend inline fun <reified T> DataStoreManager.putObject(
    key: Preferences.Key<String>,
    value: T,
): Unit = putObject(key, value, defaultDataStoreJson)

/**
 * Saves a serialized object of type [T] into [DataStoreManager] under [key] using custom [json].
 */
suspend inline fun <reified T> DataStoreManager.putObject(
    key: Preferences.Key<String>,
    value: T,
    json: Json,
) {
    val jsonString = json.encodeToString(serializer<T>(), value)
    put(key, jsonString)
}
