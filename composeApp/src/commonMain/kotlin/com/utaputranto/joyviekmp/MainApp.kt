package com.utaputranto.joyviekmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MainApp() {
    MaterialTheme {
        val navController = rememberNavController()
        AppNavigation(navController = navController)
    }
}
