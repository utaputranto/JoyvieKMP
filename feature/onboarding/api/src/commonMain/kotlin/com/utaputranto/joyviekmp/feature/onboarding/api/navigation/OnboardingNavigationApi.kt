package com.utaputranto.joyviekmp.feature.onboarding.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object SplashMainScreenRoute : NavKey

@Serializable
data object WelcomeMainScreenRoute : NavKey

fun MutableList<NavKey>.navigateToOnboarding() {
    clear()
    add(SplashMainScreenRoute)
}
