package com.utaputranto.joyviekmp.core.designsystem.atom

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

/**
 * Page indicator atom component built strictly according to Joyvie Figma spec.
 *
 * @param pageCount Total number of pages.
 * @param currentPage Currently active page index.
 * @param activeWidth Width of the active indicator dot (default 24.dp).
 * @param inactiveWidth Width of the inactive indicator dot (default 6.dp).
 * @param indicatorHeight Height of indicator dots (default 6.dp).
 * @param activeColor Fill color of active indicator (default #82E1FF / JoyvieTheme.colors.primary).
 * @param inactiveColor Fill color of inactive indicator (default #475569 / JoyvieTheme.colors.outline).
 */
@Composable
fun JoyviePageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeWidth: Dp = 24.dp,
    inactiveWidth: Dp = 6.dp,
    indicatorHeight: Dp = 6.dp,
    spacing: Dp = JoyvieTheme.dimens.spacing.small,
    activeColor: Color = JoyvieTheme.colors.primary,
    inactiveColor: Color = JoyvieTheme.colors.outline,
) {
    Row(
        modifier = modifier.padding(vertical = JoyvieTheme.dimens.spacing.small),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage

            val animatedWidth by animateDpAsState(
                targetValue = if (isActive) activeWidth else inactiveWidth,
                animationSpec = tween(durationMillis = 300),
                label = "PageIndicatorWidth",
            )

            val animatedColor by animateColorAsState(
                targetValue = if (isActive) activeColor else inactiveColor,
                animationSpec = tween(durationMillis = 300),
                label = "PageIndicatorColor",
            )

            val shape = if (isActive) RoundedCornerShape(indicatorHeight / 2) else CircleShape

            Box(
                modifier =
                    Modifier
                        .width(animatedWidth)
                        .height(indicatorHeight)
                        .clip(shape)
                        .background(animatedColor),
            )
        }
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
private fun JoyviePageIndicatorPreview(
    @androidx.compose.ui.tooling.preview.PreviewParameter(
        com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider::class,
    ) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        androidx.compose.material3.Surface(
            color = JoyvieTheme.colors.background,
        ) {
            Box(
                modifier = Modifier.padding(JoyvieTheme.dimens.spacing.medium),
                contentAlignment = Alignment.Center,
            ) {
                JoyviePageIndicator(
                    pageCount = 3,
                    currentPage = 0,
                )
            }
        }
    }
}
