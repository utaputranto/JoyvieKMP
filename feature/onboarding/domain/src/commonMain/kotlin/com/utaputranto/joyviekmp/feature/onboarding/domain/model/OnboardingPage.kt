package com.utaputranto.joyviekmp.feature.onboarding.domain.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class OnboardingPage(
    val id: Int,
    val title: StringResource,
    val titleHighlight: StringResource,
    val description: StringResource,
    val buttonText: StringResource,
    val imageBackground: DrawableResource,
)
