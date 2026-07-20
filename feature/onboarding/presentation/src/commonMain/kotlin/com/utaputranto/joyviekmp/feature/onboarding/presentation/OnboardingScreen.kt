package com.utaputranto.joyviekmp.feature.onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieButton
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieButtonStyle
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import com.utaputranto.joyviekmp.core.platform.AppLogger
import com.utaputranto.joyviekmp.core.platform.DeviceInfo
import com.utaputranto.joyviekmp.core.platform.showToast
import kotlinx.coroutines.flow.SharedFlow

private const val TAG = "OnboardingScreen"

@Composable
fun OnboardingScreen1(
    deviceInfo: DeviceInfo,
    toastEvent: SharedFlow<String>,
    onNext: () -> Unit,
    onTestEndpoint: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Listen Toast Event dari ViewModel khusus untuk Screen 1
    LaunchedEffect(Unit) {
        AppLogger.i(TAG, "Screen 1 shown on ${deviceInfo.osName} ${deviceInfo.osVersion}")

        toastEvent.collect { message ->
            showToast(message)
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(JoyvieTheme.colors.background)
                .padding(JoyvieTheme.dimens.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Welcome to Joyvie",
            style = JoyvieTheme.typography.headline,
            color = JoyvieTheme.colors.onBackground,
        )
        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.small))
        Text(
            text = "Running on ${deviceInfo.osName} ${deviceInfo.osVersion}",
            style = JoyvieTheme.typography.body,
            color = JoyvieTheme.colors.onSurfaceVariant,
        )
        Text(
            text = if (deviceInfo.isDebug) "Mode: Debug" else "Mode: Release",
            style = JoyvieTheme.typography.label,
            color = if (deviceInfo.isDebug) JoyvieTheme.colors.warning else JoyvieTheme.colors.success,
        )
        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.large))
        JoyvieButton(
            text = "Next Screen",
            onClick = {
                AppLogger.d(TAG, "Next button clicked")
                onNext()
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                AppLogger.d(TAG, "Test API endpoint button clicked")
                onTestEndpoint()
            },
        ) {
            Text("Test Hit API Endpoint")
        }
    }
}

@Composable
fun OnboardingScreen2(
    toastEvent: SharedFlow<String>,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Listen Toast Event dari ViewModel khusus untuk Screen 2
    LaunchedEffect(Unit) {
        toastEvent.collect { message ->
            showToast(message)
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(JoyvieTheme.colors.background)
                .padding(JoyvieTheme.dimens.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "You're all set!",
            style = JoyvieTheme.typography.headline,
            color = JoyvieTheme.colors.onBackground,
        )
        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.small))
        Text(
            text = "Finish onboarding to start using the app.",
            style = JoyvieTheme.typography.body,
            color = JoyvieTheme.colors.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.large))
        JoyvieButton(
            text = "Finish Onboarding",
            onClick = onFinished,
        )
        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.small))
        JoyvieButton(
            text = "Back",
            onClick = {},
            style = JoyvieButtonStyle.Secondary,
            enabled = false,
        )
    }
}
