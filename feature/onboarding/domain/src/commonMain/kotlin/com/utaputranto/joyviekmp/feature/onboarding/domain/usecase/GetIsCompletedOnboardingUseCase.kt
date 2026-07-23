package com.utaputranto.joyviekmp.feature.onboarding.domain.usecase

import com.utaputranto.joyviekmp.feature.onboarding.domain.repository.OnboardingRepository

class GetIsCompletedOnboardingUseCase(
    private val repository: OnboardingRepository,
) {
    suspend operator fun invoke(): Boolean = repository.isCompleted()
}
