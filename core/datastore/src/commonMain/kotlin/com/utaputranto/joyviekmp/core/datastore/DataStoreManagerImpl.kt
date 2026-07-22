package com.utaputranto.joyviekmp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

/**
 * Default implementation of [DataStoreManager] delegating to [DataStore<Preferences>].
 */
@Single(binds = [DataStoreManager::class])
class DataStoreManagerImpl(
    private val dataStore: DataStore<Preferences>,
) : DataStoreManager {
    override fun <T> get(
        key: Preferences.Key<T>,
        defaultValue: T,
    ): Flow<T> {
        return dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }

    override fun <T> getOrNull(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map { preferences ->
            preferences[key]
        }
    }

    override suspend fun <T> put(
        key: Preferences.Key<T>,
        value: T,
    ) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
