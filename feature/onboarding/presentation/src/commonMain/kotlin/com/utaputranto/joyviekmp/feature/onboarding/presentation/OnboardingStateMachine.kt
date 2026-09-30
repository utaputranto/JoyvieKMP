package com.utaputranto.joyviekmp.feature.onboarding.presentation

import androidx.lifecycle.viewModelScope
import com.utaputranto.joyviekmp.core.mvi.BaseStateMachine
import com.utaputranto.joyviekmp.core.platform.AppLogger
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.GetIsCompletedOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.GetOnboardingPagesUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import kotlin.time.Duration.Companion.milliseconds

/**
 * State machine managing the splash branding and onboarding welcome flow.
 *
 * Checks onboarding status upon initialization, loads onboarding pages,
 * and handles completion intents by emitting navigation effects.
 *
 * @param completeOnboarding Usecase to persist onboarding completion.
 * @param getOnboardingPages Usecase to fetch onboarding carousel content.
 * @param getIsCompletedOnboardingUseCase Usecase to check if onboarding was already completed.
 */
@KoinViewModel
class OnboardingStateMachine(
    private val completeOnboarding: CompleteOnboardingUseCase,
    private val getOnboardingPages: GetOnboardingPagesUseCase,
    private val getIsCompletedOnboardingUseCase: GetIsCompletedOnboardingUseCase,
) : BaseStateMachine<OnboardingState, OnboardingEvent, OnboardingEffect>(OnboardingState()) {
    init {
        onEvent(OnboardingEvent.CheckOnboardingStatus)
    }

    override fun onEvent(event: OnboardingEvent) {
        when (event) {
            OnboardingEvent.CheckOnboardingStatus -> checkOnboardingStatus()
            OnboardingEvent.LoadPages -> loadPages()
            OnboardingEvent.FinishOnboarding -> finishOnboarding()
        }
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            delay(1000L.milliseconds)
            if (getIsCompletedOnboardingUseCase()) {
                sendEffect(OnboardingEffect.NavigateToHome)
            } else {
                loadPages()
                sendEffect(OnboardingEffect.NavigateToWelcome)
            }
        }
    }

    private fun loadPages() {
        viewModelScope.launch {
            val pages = getOnboardingPages()
            setState { copy(pages = pages) }
        }
    }

    private fun finishOnboarding() {
        viewModelScope.launch {
            completeOnboarding()
            AppLogger.i(TAG, "Onboarding completed")
            sendEffect(OnboardingEffect.NavigateToHome)
        }
    }

    private companion object {
        const val TAG = "OnboardingStateMachine"
    }
}
