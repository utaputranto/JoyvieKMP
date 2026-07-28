package com.utaputranto.joyviekmp.feature.auth.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.utaputranto.joyviekmp.feature.auth.api.navigation.AuthScreen1Route
import com.utaputranto.joyviekmp.feature.auth.api.navigation.AuthScreen2Route
import com.utaputranto.joyviekmp.feature.auth.presentation.AuthScreen1
import com.utaputranto.joyviekmp.feature.auth.presentation.AuthScreen2
import com.utaputranto.joyviekmp.feature.auth.presentation.AuthViewModel
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.navigateToOnboarding
import org.koin.compose.viewmodel.koinViewModel

/**
 * Registers this feature's screens as Nav3 entries against a shared back stack.
 * Called from the app-level NavDisplay entryProvider.
 */
fun EntryProviderScope<NavKey>.authEntries(backStack: MutableList<NavKey>) {
    entry<AuthScreen1Route> {
        val viewModel = koinViewModel<AuthViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        AuthScreen1(
            uiState = uiState,
            onEmailChanged = viewModel::onEmailChanged,
            onPasswordChanged = viewModel::onPasswordChanged,
            onLogin = viewModel::onLoginClicked,
            onNext = { backStack.add(AuthScreen2Route) },
        )
    }
    entry<AuthScreen2Route> {
        AuthScreen2(
            onResetOnboarding = {
                backStack.navigateToOnboarding()
            },
        )
    }
}
