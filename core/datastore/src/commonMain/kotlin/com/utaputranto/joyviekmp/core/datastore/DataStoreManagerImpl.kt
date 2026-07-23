package com.utaputranto.joyviekmp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManagerImpl(
    private val dataStore: DataStore<Preferences>,
) : PreferenceStorage {
    private val onboardingCompletedKey = booleanPreferencesKey("onboarding_completed")

    override fun isCompletedOnboarding(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[onboardingCompletedKey] ?: false
        }
    }

    override suspend fun completeOnboarding(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[onboardingCompletedKey] = completed
        }
    }
}
