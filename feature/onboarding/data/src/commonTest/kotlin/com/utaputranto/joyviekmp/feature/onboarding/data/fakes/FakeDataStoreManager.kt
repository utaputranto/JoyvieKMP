package com.utaputranto.joyviekmp.feature.onboarding.data.fakes

import androidx.datastore.preferences.core.Preferences
import com.utaputranto.joyviekmp.core.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeDataStoreManager : DataStoreManager {
    private val preferencesMap = MutableStateFlow<Map<Preferences.Key<*>, Any>>(emptyMap())

    override fun <T> get(
        key: Preferences.Key<T>,
        defaultValue: T,
    ): Flow<T> {
        return preferencesMap.map { map ->
            @Suppress("UNCHECKED_CAST")
            (map[key] as? T) ?: defaultValue
        }
    }

    override fun <T> getOrNull(key: Preferences.Key<T>): Flow<T?> {
        return preferencesMap.map { map ->
            @Suppress("UNCHECKED_CAST")
            map[key] as? T
        }
    }

    override suspend fun <T> put(
        key: Preferences.Key<T>,
        value: T,
    ) {
        preferencesMap.value += (key to (value as Any))
    }

    override suspend fun <T> remove(key: Preferences.Key<T>) {
        preferencesMap.value -= key
    }

    override suspend fun clear() {
        preferencesMap.value = emptyMap()
    }
}
