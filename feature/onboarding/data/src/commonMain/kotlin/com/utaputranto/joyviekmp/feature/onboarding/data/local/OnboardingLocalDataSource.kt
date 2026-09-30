package com.utaputranto.joyviekmp.feature.onboarding.data.local

import kotlinx.coroutines.flow.Flow

interface OnboardingLocalDataSource {
    fun isCompletedOnboarding(): Flow<Boolean>

    suspend fun completeOnboarding(completed: Boolean)
}
