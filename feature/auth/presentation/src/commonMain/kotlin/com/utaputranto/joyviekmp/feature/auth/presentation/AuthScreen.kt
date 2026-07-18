package com.utaputranto.joyviekmp.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen1(
    uiState: AuthUiState,
    onLogin: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Auth - Screen 1 (Login)")
        Spacer(modifier = Modifier.height(16.dp))
        when {
            uiState.isLoading -> CircularProgressIndicator()
            uiState.user != null -> Text("Logged in as ${uiState.user.name} (${uiState.user.email})")
            else ->
                Button(onClick = onLogin) {
                    Text("Login as Test User")
                }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNext) {
            Text("Register Screen")
        }
    }
}

@Composable
fun AuthScreen2(
    onResetOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Auth - Screen 2 (Register)")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onResetOnboarding) {
            Text("Go back to Onboarding")
        }
    }
}
