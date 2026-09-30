package com.utaputranto.joyviekmp.feature.onboarding.presentation

import com.utaputranto.joyviekmp.core.mvi.UiEffect
import com.utaputranto.joyviekmp.core.mvi.UiEvent
import com.utaputranto.joyviekmp.core.mvi.UiState
import com.utaputranto.joyviekmp.feature.onboarding.domain.model.OnboardingPage

/**
 * Immutable UI state for the onboarding feature.
 *
 * @param pages List of onboarding carousel pages.
 */
data class OnboardingState(
    val pages: List<OnboardingPage> = emptyList(),
) : UiState

/**
 * User intents and UI events for the onboarding feature.
 */
sealed interface OnboardingEvent : UiEvent {
    data object CheckOnboardingStatus : OnboardingEvent

    data object LoadPages : OnboardingEvent

    data object FinishOnboarding : OnboardingEvent
}

/**
 * One-shot side effects (such as navigation) for the onboarding feature.
 */
sealed interface OnboardingEffect : UiEffect {
    data object NavigateToHome : OnboardingEffect

    data object NavigateToWelcome : OnboardingEffect
}
