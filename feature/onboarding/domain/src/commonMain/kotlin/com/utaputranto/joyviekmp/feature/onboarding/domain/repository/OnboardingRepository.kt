package com.utaputranto.joyviekmp.feature.onboarding.domain.repository

interface OnboardingRepository {
    suspend fun isCompleted(): Boolean

    suspend fun completeOnboarding()
}
