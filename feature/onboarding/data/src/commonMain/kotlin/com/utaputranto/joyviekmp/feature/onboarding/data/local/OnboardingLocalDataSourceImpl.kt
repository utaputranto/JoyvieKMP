package com.utaputranto.joyviekmp.feature.onboarding.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single(binds = [OnboardingLocalDataSource::class])
class OnboardingLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : OnboardingLocalDataSource {
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
