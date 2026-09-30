package com.utaputranto.joyviekmp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

@Composable
fun MainApp() {
    JoyvieTheme(darkTheme = isSystemInDarkTheme()) {
        AppNavigation()
    }
}
