package com.utaputranto.joyviekmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.utaputranto.joyviekmp.feature.auth.presentation.navigation.authGraph
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.OnboardingRoute
import com.utaputranto.joyviekmp.feature.onboarding.presentation.navigation.onboardingGraph

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = OnboardingRoute,
        modifier = modifier,
    ) {
        onboardingGraph(navController = navController)
        authGraph(navController = navController)
    }
}
