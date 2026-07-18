package com.utaputranto.joyviekmp.feature.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieButton
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieButtonStyle
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieTextField
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

@Composable
fun AuthScreen1(
    uiState: AuthUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLogin: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            text = "Sign in to Joyvie",
            style = JoyvieTheme.typography.headline,
            color = JoyvieTheme.colors.onBackground,
        )
        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.small))
        if (uiState.user != null) {
            Text(
                text = "Logged in as ${uiState.user.name} (${uiState.user.email})",
                style = JoyvieTheme.typography.body,
                color = JoyvieTheme.colors.success,
            )
        } else {
            Text(
                text = "Enter your credentials to continue.",
                style = JoyvieTheme.typography.body,
                color = JoyvieTheme.colors.onSurfaceVariant,
            )
        }
        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.large))
        JoyvieTextField(
            value = uiState.email,
            onValueChange = onEmailChanged,
            label = "Email",
            placeholder = "you@example.com",
            enabled = uiState.user == null && !uiState.isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.small))
        JoyvieTextField(
            value = uiState.password,
            onValueChange = onPasswordChanged,
            label = "Password",
            enabled = uiState.user == null && !uiState.isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.large))
        JoyvieButton(
            text = if (uiState.user != null) "Logged In" else "Login",
            onClick = onLogin,
            loading = uiState.isLoading,
            enabled = uiState.canSubmit,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.small))
        JoyvieButton(
            text = "Register Screen",
            onClick = onNext,
            style = JoyvieButtonStyle.Secondary,
        )
    }
}

@Composable
fun AuthScreen2(
    onResetOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            text = "Create your account",
            style = JoyvieTheme.typography.headline,
            color = JoyvieTheme.colors.onBackground,
        )
        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.small))
        Text(
            text = "Registration is coming soon.",
            style = JoyvieTheme.typography.body,
            color = JoyvieTheme.colors.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.large))
        JoyvieButton(
            text = "Go back to Onboarding",
            onClick = onResetOnboarding,
            style = JoyvieButtonStyle.Secondary,
        )
    }
}
