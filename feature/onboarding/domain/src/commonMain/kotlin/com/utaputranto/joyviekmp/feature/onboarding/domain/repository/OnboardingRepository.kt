package com.utaputranto.joyviekmp.feature.onboarding.domain.repository

import com.utaputranto.joyviekmp.feature.onboarding.domain.model.OnboardingPage

interface OnboardingRepository {
    suspend fun isCompleted(): Boolean

    suspend fun completeOnboarding()

    suspend fun getOnboardingPages(): List<OnboardingPage>
}
