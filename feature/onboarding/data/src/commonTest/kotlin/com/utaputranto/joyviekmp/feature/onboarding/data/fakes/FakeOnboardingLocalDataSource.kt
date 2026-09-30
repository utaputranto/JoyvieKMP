package com.utaputranto.joyviekmp.feature.onboarding.data.fakes

import com.utaputranto.joyviekmp.feature.onboarding.data.local.OnboardingLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeOnboardingLocalDataSource : OnboardingLocalDataSource {
    private val isCompletedOnboardingFlow = MutableStateFlow(false)

    override fun isCompletedOnboarding(): Flow<Boolean> {
        return isCompletedOnboardingFlow.asStateFlow()
    }

    override suspend fun completeOnboarding(completed: Boolean) {
        isCompletedOnboardingFlow.value = completed
    }
}
