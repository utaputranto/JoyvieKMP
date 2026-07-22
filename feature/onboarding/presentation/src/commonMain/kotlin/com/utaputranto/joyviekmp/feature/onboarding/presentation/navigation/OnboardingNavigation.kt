package com.utaputranto.joyviekmp.feature.onboarding.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.utaputranto.joyviekmp.core.mvi.rememberSharedStateMachine
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.SplashMainScreenRoute
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.WelcomeMainScreenRoute
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.navigateToWelcome
import com.utaputranto.joyviekmp.feature.onboarding.presentation.OnboardingEffect
import com.utaputranto.joyviekmp.feature.onboarding.presentation.OnboardingStateMachine
import com.utaputranto.joyviekmp.feature.onboarding.presentation.screen.splash.SplashMainScreen
import com.utaputranto.joyviekmp.feature.onboarding.presentation.screen.welcome.WelcomeMainScreen

fun EntryProviderScope<NavKey>.onboardingEntries(
    backStack: MutableList<NavKey>,
    viewModelStoreOwner: ViewModelStoreOwner? = null,
) {
    entry<SplashMainScreenRoute> {
        val stateMachine = rememberSharedStateMachine<OnboardingStateMachine>(viewModelStoreOwner)
        OnboardingEffectHandler(stateMachine, backStack)

        SplashMainScreen()
    }
    entry<WelcomeMainScreenRoute> {
        val stateMachine = rememberSharedStateMachine<OnboardingStateMachine>(viewModelStoreOwner)
        val state by stateMachine.state.collectAsStateWithLifecycle()
        OnboardingEffectHandler(stateMachine, backStack)

        WelcomeMainScreen(
            state = state,
            onEvent = stateMachine::onEvent,
        )
    }
}

@Composable
private fun OnboardingEffectHandler(
    stateMachine: OnboardingStateMachine,
    backStack: MutableList<NavKey>,
) {
    LaunchedEffect(stateMachine.effect) {
        stateMachine.effect.collect { effect ->
            when (effect) {
                OnboardingEffect.NavigateToHome -> {
                    // TODO: Navigate to Home feature when Home API is available
                }

                OnboardingEffect.NavigateToWelcome -> backStack.navigateToWelcome()
            }
        }
    }
}
