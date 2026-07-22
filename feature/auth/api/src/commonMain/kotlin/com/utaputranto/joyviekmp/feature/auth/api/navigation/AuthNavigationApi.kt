package com.utaputranto.joyviekmp.feature.auth.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object AuthScreen1Route : NavKey

@Serializable
data object AuthScreen2Route : NavKey

fun MutableList<NavKey>.navigateToAuth() {
    clear()
    add(AuthScreen1Route)
}
