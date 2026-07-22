package com.utaputranto.joyviekmp.feature.onboarding.data.repository

import com.utaputranto.joyviekmp.feature.onboarding.data.local.OnboardingLocalDataSource
import com.utaputranto.joyviekmp.feature.onboarding.domain.model.OnboardingPage
import com.utaputranto.joyviekmp.feature.onboarding.domain.repository.OnboardingRepository
import joyviekmp.feature.onboarding.data.generated.resources.Res
import joyviekmp.feature.onboarding.data.generated.resources.ic_onboarding_step1_trending
import joyviekmp.feature.onboarding.data.generated.resources.ic_onboarding_step2_search
import joyviekmp.feature.onboarding.data.generated.resources.ic_onboarding_step3_watchlist
import joyviekmp.feature.onboarding.data.generated.resources.onboarding_button_get_started
import joyviekmp.feature.onboarding.data.generated.resources.onboarding_button_next
import joyviekmp.feature.onboarding.data.generated.resources.onboarding_step1_description
import joyviekmp.feature.onboarding.data.generated.resources.onboarding_step1_title
import joyviekmp.feature.onboarding.data.generated.resources.onboarding_step1_title_highlight
import joyviekmp.feature.onboarding.data.generated.resources.onboarding_step2_description
import joyviekmp.feature.onboarding.data.generated.resources.onboarding_step2_title
import joyviekmp.feature.onboarding.data.generated.resources.onboarding_step2_title_highlight
import joyviekmp.feature.onboarding.data.generated.resources.onboarding_step3_description
import joyviekmp.feature.onboarding.data.generated.resources.onboarding_step3_title
import joyviekmp.feature.onboarding.data.generated.resources.onboarding_step3_title_highlight
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

@Single(binds = [OnboardingRepository::class])
class OnboardingRepositoryImpl(
    private val localDataSource: OnboardingLocalDataSource,
) : OnboardingRepository {
    override suspend fun isCompleted(): Boolean {
        return localDataSource.isCompletedOnboarding().first()
    }

    override suspend fun completeOnboarding() {
        localDataSource.completeOnboarding(true)
    }

    override suspend fun getOnboardingPages(): List<OnboardingPage> {
        return listOf(
            OnboardingPage(
                id = 1,
                title = Res.string.onboarding_step1_title,
                titleHighlight = Res.string.onboarding_step1_title_highlight,
                description = Res.string.onboarding_step1_description,
                buttonText = Res.string.onboarding_button_next,
                imageBackground = Res.drawable.ic_onboarding_step1_trending,
            ),
            OnboardingPage(
                id = 2,
                title = Res.string.onboarding_step2_title,
                titleHighlight = Res.string.onboarding_step2_title_highlight,
                description = Res.string.onboarding_step2_description,
                buttonText = Res.string.onboarding_button_next,
                imageBackground = Res.drawable.ic_onboarding_step2_search,
            ),
            OnboardingPage(
                id = 3,
                title = Res.string.onboarding_step3_title,
                titleHighlight = Res.string.onboarding_step3_title_highlight,
                description = Res.string.onboarding_step3_description,
                buttonText = Res.string.onboarding_button_get_started,
                imageBackground = Res.drawable.ic_onboarding_step3_watchlist,
            ),
        )
    }
}
