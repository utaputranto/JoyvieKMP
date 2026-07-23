package com.utaputranto.joyviekmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.utaputranto.joyviekmp.feature.auth.presentation.navigation.authEntries
import com.utaputranto.joyviekmp.feature.onboarding.api.navigation.SplashMainScreenRoute
import com.utaputranto.joyviekmp.feature.onboarding.presentation.navigation.onboardingEntries

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = remember { NavBackStack<NavKey>(SplashMainScreenRoute) }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators =
            listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
        entryProvider =
            entryProvider {
                onboardingEntries(backStack)
                authEntries(backStack)
            },
    )
}
