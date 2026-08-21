package com.utaputranto.joyviekmp.feature.home.presentation.screen.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

@Composable
fun ProfilePlaceholderScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Profile coming soon",
            style = JoyvieTheme.typography.body,
            color = JoyvieTheme.colors.onSurfaceVariant,
        )
    }
}
