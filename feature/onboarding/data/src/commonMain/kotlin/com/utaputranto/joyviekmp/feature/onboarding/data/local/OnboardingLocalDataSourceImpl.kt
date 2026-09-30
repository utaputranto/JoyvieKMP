package com.utaputranto.joyviekmp.feature.onboarding.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.utaputranto.joyviekmp.core.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single(binds = [OnboardingLocalDataSource::class])
class OnboardingLocalDataSourceImpl(
    private val dataStoreManager: DataStoreManager,
) : OnboardingLocalDataSource {
    private val onboardingCompletedKey = booleanPreferencesKey("onboarding_completed")

    override fun isCompletedOnboarding(): Flow<Boolean> {
        return dataStoreManager.get(onboardingCompletedKey, defaultValue = false)
    }

    override suspend fun completeOnboarding(completed: Boolean) {
        dataStoreManager.put(onboardingCompletedKey, completed)
    }
}
