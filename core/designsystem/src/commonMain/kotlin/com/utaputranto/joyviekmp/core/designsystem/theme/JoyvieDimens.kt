package com.utaputranto.joyviekmp.core.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Joyvie Design System Dimension tokens grouped by domain category (Spacing, IconSize, CornerRadius).
 */
@Stable
data class JoyvieDimens(
    val spacing: Spacing = Spacing(),
    val icon: IconSize = IconSize(),
    val radius: CornerRadius = CornerRadius(),
) {
    @Stable
    data class Spacing(
        val none: Dp = 0.dp,
        val xxsmall: Dp = 2.dp,
        val xsmall: Dp = 4.dp,
        val small: Dp = 8.dp,
        val mediumSmall: Dp = 12.dp,
        val medium: Dp = 16.dp,
        val large: Dp = 24.dp,
        val xlarge: Dp = 32.dp,
        val xxlarge: Dp = 36.dp,
        val xxxlarge: Dp = 48.dp,
        val huge: Dp = 72.dp,
    )

    @Stable
    data class IconSize(
        val small: Dp = 16.dp,
        val medium: Dp = 24.dp,
        val brandSmall: Dp = 28.dp,
        val large: Dp = 32.dp,
        val brandLarge: Dp = 72.dp,
    )

    @Stable
    data class CornerRadius(
        val small: Dp = 4.dp,
        val medium: Dp = 8.dp,
        val large: Dp = 16.dp,
        val xlarge: Dp = 24.dp,
        val full: Dp = 999.dp,
    )
}
