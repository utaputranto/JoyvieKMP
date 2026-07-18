package com.utaputranto.joyviekmp.feature.auth.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.utaputranto.joyviekmp.feature.auth.api.navigation.AuthRoute
import com.utaputranto.joyviekmp.feature.auth.api.navigation.AuthScreen1Route
import com.utaputranto.joyviekmp.feature.auth.api.navigation.AuthScreen2Route
import com.utaputranto.joyviekmp.feature.auth.presentation.AuthScreen1
import com.utaputranto.joyviekmp.feature.auth.presentation.AuthScreen2
import com.utaputranto.joyviekmp.feature.auth.presentation.AuthViewModel
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.navigateToOnboarding
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation<AuthRoute>(startDestination = AuthScreen1Route) {
        composable<AuthScreen1Route> {
            val viewModel = koinViewModel<AuthViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            AuthScreen1(
                uiState = uiState,
                onEmailChanged = viewModel::onEmailChanged,
                onPasswordChanged = viewModel::onPasswordChanged,
                onLogin = viewModel::onLoginClicked,
                onNext = { navController.navigate(AuthScreen2Route) },
            )
        }
        composable<AuthScreen2Route> {
            AuthScreen2(
                onResetOnboarding = {
                    navController.navigateToOnboarding()
                },
            )
        }
    }
}
