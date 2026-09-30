package com.utaputranto.joyviekmp.feature.onboarding.domain.usecase

import com.utaputranto.joyviekmp.feature.onboarding.domain.repository.OnboardingRepository
import org.koin.core.annotation.Factory

@Factory
class CompleteOnboardingUseCase(
    private val repository: OnboardingRepository,
) {
    suspend operator fun invoke() = repository.completeOnboarding()
}
