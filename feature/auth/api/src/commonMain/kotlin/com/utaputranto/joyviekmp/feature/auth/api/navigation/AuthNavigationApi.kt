package com.utaputranto.joyviekmp.feature.auth.api.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.serialization.Serializable

@Serializable
object AuthRoute

@Serializable
object AuthScreen1Route

@Serializable
object AuthScreen2Route

fun NavController.navigateToAuth() {
    val route = graph.findStartDestination().route
    navigate(AuthRoute) {
        if (route != null) {
            popUpTo(route) {
                inclusive = true
            }
        }
    }
}
