package com.utaputranto.joyviekmp.feature.onboarding.presentation.screen.welcome

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.utaputranto.joyviekmp.feature.onboarding.presentation.OnboardingEvent
import com.utaputranto.joyviekmp.feature.onboarding.presentation.OnboardingState
import kotlinx.coroutines.launch

/**
 * WelcomeMainScreen composable container strictly matching Joyvie Figma specs.
 *
 * Manages horizontal pager state and system back navigation events (predictive back),
 * delegating pure UI layout rendering to [WelcomeScreen].
 *
 * @param modifier [Modifier] applied to the root container.
 * @param state Immutable [OnboardingState] containing onboarding UI state.
 * @param onEvent Callback to dispatch user intents ([OnboardingEvent]) to the StateMachine.
 */
@Composable
fun WelcomeMainScreen(
    modifier: Modifier = Modifier,
    state: OnboardingState,
    onEvent: (OnboardingEvent) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { state.pages.size })
    val coroutineScope = rememberCoroutineScope()

    if (LocalNavigationEventDispatcherOwner.current != null) {
        NavigationBackHandler(
            state = rememberNavigationEventState(NavigationEventInfo.None),
            isBackEnabled = pagerState.currentPage > 0,
            onBackCompleted = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            },
        )
    }

    WelcomeScreen(
        state = state,
        pagerState = pagerState,
        onEvent = onEvent,
        onNext = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        },
    )
}
