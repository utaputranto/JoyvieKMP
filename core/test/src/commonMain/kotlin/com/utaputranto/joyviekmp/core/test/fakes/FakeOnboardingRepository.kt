package com.utaputranto.joyviekmp.core.test.fakes

import com.utaputranto.joyviekmp.feature.onboarding.domain.model.OnboardingPage
import com.utaputranto.joyviekmp.feature.onboarding.domain.repository.OnboardingRepository

class FakeOnboardingRepository : OnboardingRepository {
    private var isCompleted = false
    private var pages: List<OnboardingPage> = emptyList()

    fun setCompleted(completed: Boolean) {
        isCompleted = completed
    }

    fun setPages(pages: List<OnboardingPage>) {
        this.pages = pages
    }

    override suspend fun isCompleted(): Boolean {
        return isCompleted
    }

    override suspend fun completeOnboarding() {
        isCompleted = true
    }

    override suspend fun getOnboardingPages(): List<OnboardingPage> {
        return pages
    }
}
