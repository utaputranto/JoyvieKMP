package com.utaputranto.joyviekmp.feature.onboarding.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.utaputranto.joyviekmp.feature.auth.api.navigation.navigateToAuth
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.OnboardingRoute
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.OnboardingScreen1Route
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.OnboardingScreen2Route
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.SplashRoute
import com.utaputranto.joyviekmp.feature.onboarding.presentation.OnboardingScreen1
import com.utaputranto.joyviekmp.feature.onboarding.presentation.OnboardingScreen2
import com.utaputranto.joyviekmp.feature.onboarding.presentation.OnboardingViewModel
import com.utaputranto.joyviekmp.feature.onboarding.presentation.SplashScreen
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.onboardingGraph(navController: NavController) {
    navigation<OnboardingRoute>(startDestination = SplashRoute) {
        composable<SplashRoute> {
            val viewModel = koinViewModel<OnboardingViewModel>()
            SplashScreen(
                isOnboardingCompletedFlow = viewModel.isOnboardingCompleted,
                onCheckStatus = { viewModel.checkOnboardingStatus() },
                onNavigateToAuth = { navController.navigateToAuth() },
                onNavigateToOnboarding = {
                    navController.navigate(OnboardingScreen1Route) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                },
            )
        }
        composable<OnboardingScreen1Route> {
            val viewModel = koinViewModel<OnboardingViewModel>()
            OnboardingScreen1(
                deviceInfo = viewModel.deviceInfo,
                toastEvent = viewModel.toastEvent,
                onNext = { navController.navigate(OnboardingScreen2Route) },
                onTestEndpoint = { viewModel.testHitEndpoint() },
            )
        }
        composable<OnboardingScreen2Route> {
            val viewModel = koinViewModel<OnboardingViewModel>()
            OnboardingScreen2(
                toastEvent = viewModel.toastEvent,
                onFinished = {
                    viewModel.finishOnboarding {
                        navController.navigateToAuth()
                    }
                },
            )
        }
    }
}
