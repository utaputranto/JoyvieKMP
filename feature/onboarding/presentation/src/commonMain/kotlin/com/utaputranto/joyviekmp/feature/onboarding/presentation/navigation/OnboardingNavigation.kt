package com.utaputranto.joyviekmp.feature.onboarding.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.utaputranto.joyviekmp.feature.auth.api.navigation.navigateToAuth
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.OnboardingRoute
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.SplashMainScreenRoute
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.WelcomeMainScreenRoute
import com.utaputranto.joyviekmp.feature.onboarding.presentation.OnboardingViewModel
import com.utaputranto.joyviekmp.feature.onboarding.presentation.SplashMainScreen
import com.utaputranto.joyviekmp.feature.onboarding.presentation.WelcomeMainScreen
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

fun NavGraphBuilder.onboardingGraph(navController: NavController) {
    navigation<OnboardingRoute>(startDestination = SplashMainScreenRoute) {
        composable<SplashMainScreenRoute> {
            LaunchedEffect(Unit) {
                delay(1000L.milliseconds)
                navController.navigate(WelcomeMainScreenRoute) {
                    popUpTo(SplashMainScreenRoute) { inclusive = true }
                }
            }
            SplashMainScreen()
        }
        composable<WelcomeMainScreenRoute> {
            val viewModel = koinViewModel<OnboardingViewModel>()
            val pages by viewModel.pages.collectAsStateWithLifecycle()

            WelcomeMainScreen(
                pages = pages,
                onSkip = {
                    viewModel.finishOnboarding {
                        navController.navigateToAuth()
                    }
                },
                onFinish = {
                    viewModel.finishOnboarding {
                        navController.navigateToAuth()
                    }
                },
            )
        }
    }
}
