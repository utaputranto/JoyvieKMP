package com.utaputranto.joyviekmp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

@Composable
fun MainApp() {
    JoyvieTheme(darkTheme = isSystemInDarkTheme()) {
        val navController = rememberNavController()
        AppNavigation(navController = navController)
    }
}
