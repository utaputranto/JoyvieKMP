package com.utaputranto.joyviekmp.feature.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SplashScreen(
    isOnboardingCompletedFlow: StateFlow<Boolean?>,
    onCheckStatus: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isOnboardingCompleted by isOnboardingCompletedFlow.collectAsState()

    LaunchedEffect(Unit) {
        onCheckStatus()
    }

    LaunchedEffect(isOnboardingCompleted) {
        when (isOnboardingCompleted) {
            true -> onNavigateToAuth()
            false -> onNavigateToOnboarding()
            null -> Unit // Still checking
        }
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(JoyvieTheme.colors.background),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Joyvie",
            style = JoyvieTheme.typography.headline,
            color = JoyvieTheme.colors.primary,
        )
    }
}
