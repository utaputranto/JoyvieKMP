package com.utaputranto.joyviekmp.core.datastore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

/**
 * Interface abstraction for DataStore operations across the application,
 * preventing direct coupling to DataStore<Preferences> in data sources.
 */
interface DataStoreManager {
    /**
     * Reads a preference value for [key], emitting [defaultValue] if the key is not set.
     */
    fun <T> get(
        key: Preferences.Key<T>,
        defaultValue: T,
    ): Flow<T>

    /**
     * Reads a preference value for [key], emitting null if the key is not set.
     */
    fun <T> getOrNull(key: Preferences.Key<T>): Flow<T?>

    /**
     * Saves a preference value for [key].
     */
    suspend fun <T> put(
        key: Preferences.Key<T>,
        value: T,
    )

    /**
     * Removes a preference entry for [key].
     */
    suspend fun <T> remove(key: Preferences.Key<T>)

    /**
     * Clears all preference entries.
     */
    suspend fun clear()
}
