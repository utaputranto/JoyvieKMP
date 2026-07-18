package com.utaputranto.joyviekmp.feature.onboarding.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.utaputranto.joyviekmp.core.platform.AppLogger
import com.utaputranto.joyviekmp.core.platform.DeviceInfo

private const val TAG = "OnboardingScreen1"

@Composable
fun OnboardingScreen1(
    deviceInfo: DeviceInfo,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        AppLogger.i(TAG, "Screen shown on ${deviceInfo.osName} ${deviceInfo.osVersion}")
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Welcome to Onboarding - Screen 1")
        Text("Running on ${deviceInfo.osName} ${deviceInfo.osVersion}")
        Text(if (deviceInfo.isDebug) "Mode: Debug" else "Mode: Release")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                AppLogger.d(TAG, "Next button clicked")
                onNext()
            },
        ) {
            Text("Next Screen")
        }
    }
}

@Composable
fun OnboardingScreen2(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Onboarding Complete - Screen 2")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onFinished) {
            Text("Finish Onboarding")
        }
    }
}
