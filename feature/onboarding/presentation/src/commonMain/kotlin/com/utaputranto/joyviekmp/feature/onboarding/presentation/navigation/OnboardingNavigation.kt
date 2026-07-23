package com.utaputranto.joyviekmp.feature.onboarding.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.utaputranto.joyviekmp.core.platform.showToast
import com.utaputranto.joyviekmp.feature.auth.api.navigation.navigateToAuth
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.SplashMainScreenRoute
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.WelcomeMainScreenRoute
import com.utaputranto.joyviekmp.feature.onboarding.presentation.OnboardingViewModel
import com.utaputranto.joyviekmp.feature.onboarding.presentation.SplashMainScreen
import com.utaputranto.joyviekmp.feature.onboarding.presentation.WelcomeMainScreen
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

fun EntryProviderScope<NavKey>.onboardingEntries(backStack: MutableList<NavKey>) {
    entry<SplashMainScreenRoute> {
        LaunchedEffect(Unit) {
            delay(1000L.milliseconds)
            backStack.remove(SplashMainScreenRoute)
            backStack.add(WelcomeMainScreenRoute)
        }
        SplashMainScreen()
    }
    entry<WelcomeMainScreenRoute> {
        val viewModel = koinViewModel<OnboardingViewModel>()
        val pages by viewModel.pages.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel.toastEvent) {
            viewModel.toastEvent.collect { message ->
                showToast(message)
            }
        }

        WelcomeMainScreen(
            pages = pages,
            onSkip = {
                viewModel.finishOnboarding {
                    backStack.navigateToAuth()
                }
            },
            onFinish = {
                viewModel.finishOnboarding {
                    backStack.navigateToAuth()
                }
            },
        )
    }
}
