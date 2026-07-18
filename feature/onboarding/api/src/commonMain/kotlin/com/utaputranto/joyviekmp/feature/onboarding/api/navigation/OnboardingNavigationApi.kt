package com.utaputranto.joyviekmp.feature.onboarding.api.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.serialization.Serializable

@Serializable
object OnboardingRoute

@Serializable
object OnboardingScreen1Route

@Serializable
object OnboardingScreen2Route

fun NavController.navigateToOnboarding() {
    val route = graph.findStartDestination().route
    navigate(OnboardingRoute) {
        if (route != null) {
            popUpTo(route) {
                inclusive = true
            }
        }
    }
}
