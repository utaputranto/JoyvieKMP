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
 * Reads a serialized object of type [T] from [DataStore] for a given [key].
 * Returns a [Flow] emitting null if the key doesn't exist or deserialization fails.
 */
inline fun <reified T> DataStore<Preferences>.getObject(
    key: Preferences.Key<String>,
    json: Json = defaultDataStoreJson,
): Flow<T?> {
    return data.map { preferences ->
        preferences.getObject<T>(key, json)
    }
}

/**
 * Reads a serialized object of type [T] from [Preferences] for a given [key].
 * Returns null if the key doesn't exist or deserialization fails.
 */
inline fun <reified T> Preferences.getObject(
    key: Preferences.Key<String>,
    json: Json = defaultDataStoreJson,
): T? {
    val jsonString = this[key] ?: return null
    return runCatching {
        json.decodeFromString<T>(jsonString)
    }.getOrNull()
}

/**
 * Saves a serialized object of type [T] into [DataStore] under [key].
 */
suspend inline fun <reified T> DataStore<Preferences>.putObject(
    key: Preferences.Key<String>,
    value: T,
    json: Json = defaultDataStoreJson,
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
