package com.utaputranto.joyviekmp.feature.onboarding.data.repository

import com.utaputranto.joyviekmp.feature.onboarding.domain.repository.OnboardingRepository

class OnboardingRepositoryImpl : OnboardingRepository {
    private var completed = false

    override suspend fun isCompleted(): Boolean {
        return completed
    }

    override suspend fun completeOnboarding() {
        completed = true
    }
}
