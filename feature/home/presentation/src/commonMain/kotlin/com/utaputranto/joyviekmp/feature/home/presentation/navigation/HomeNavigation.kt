package com.utaputranto.joyviekmp.feature.home.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.utaputranto.joyviekmp.feature.home.api.navigation.HomeRoute
import com.utaputranto.joyviekmp.feature.home.presentation.HomeStateMachine
import com.utaputranto.joyviekmp.feature.home.presentation.screen.home.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

/**
 * Registers this feature's screen as a Nav3 entry against a shared back stack.
 * Called from the app-level NavDisplay entryProvider.
 */
fun EntryProviderScope<NavKey>.homeEntries(backStack: MutableList<NavKey>) {
    entry<HomeRoute> {
        val stateMachine = koinViewModel<HomeStateMachine>()
        val state by stateMachine.state.collectAsStateWithLifecycle()

        HomeScreen(
            state = state,
            onEvent = stateMachine::onEvent,
        )
    }
}
