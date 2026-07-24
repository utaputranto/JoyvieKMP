package com.utaputranto.joyviekmp.core.test.fakes

import com.utaputranto.joyviekmp.core.datastore.PreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakePreferenceStorage : PreferenceStorage {
    private val isCompletedOnboardingFlow = MutableStateFlow(false)

    override fun isCompletedOnboarding(): Flow<Boolean> {
        return isCompletedOnboardingFlow.asStateFlow()
    }

    override suspend fun completeOnboarding(completed: Boolean) {
        isCompletedOnboardingFlow.value = completed
    }
}
