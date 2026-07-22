package com.utaputranto.joyviekmp.feature.onboarding.domain.usecase

import com.utaputranto.joyviekmp.feature.onboarding.domain.model.OnboardingPage
import com.utaputranto.joyviekmp.feature.onboarding.domain.repository.OnboardingRepository

class GetOnboardingPagesUseCase(
    private val repository: OnboardingRepository,
) {
    suspend operator fun invoke(): List<OnboardingPage> {
        return repository.getOnboardingPages()
    }
}
