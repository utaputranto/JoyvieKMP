package com.utaputranto.joyviekmp.feature.onboarding.domain.usecase

import com.utaputranto.joyviekmp.feature.onboarding.domain.repository.OnboardingRepository

class CheckOnboardingUseCase(
    private val repository: OnboardingRepository,
) {
    suspend operator fun invoke() = repository.isCompleted()
}
