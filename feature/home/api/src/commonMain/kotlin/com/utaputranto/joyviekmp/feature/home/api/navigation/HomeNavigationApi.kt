package com.utaputranto.joyviekmp.feature.home.api.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute : NavKey

fun MutableList<NavKey>.navigateToHome() {
    clear()
    add(HomeRoute)
}
