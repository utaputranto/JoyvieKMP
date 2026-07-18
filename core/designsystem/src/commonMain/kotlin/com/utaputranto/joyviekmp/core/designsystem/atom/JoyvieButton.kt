package com.utaputranto.joyviekmp.core.designsystem.atom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieColors
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider

enum class JoyvieButtonStyle {
    Primary,
    Secondary,
}

@Composable
fun JoyvieButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    style: JoyvieButtonStyle = JoyvieButtonStyle.Primary,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val isClickable = enabled && !loading
    val colors = JoyvieTheme.colors
    val containerColor = getButtonContainerColor(style, isClickable, colors)
    val contentColor = getButtonContentColor(style, isClickable, colors)
    val border = getButtonBorder(style, isClickable, colors)

    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp),
        enabled = isClickable,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = containerColor,
                disabledContentColor = contentColor,
            ),
        border = border,
        shape = RoundedCornerShape(24.dp),
        contentPadding =
            PaddingValues(
                horizontal = JoyvieTheme.dimens.medium,
                vertical = JoyvieTheme.dimens.small,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = contentColor,
                    strokeWidth = 2.dp,
                )
                Spacer(modifier = Modifier.width(JoyvieTheme.dimens.small))
            } else if (leadingIcon != null) {
                Box(modifier = Modifier.size(18.dp)) {
                    leadingIcon()
                }
                Spacer(modifier = Modifier.width(JoyvieTheme.dimens.small))
            }

            Text(
                text = text,
                style = JoyvieTheme.typography.label,
            )
        }
    }
}

private fun getButtonContainerColor(
    style: JoyvieButtonStyle,
    isClickable: Boolean,
    colors: JoyvieColors,
): Color {
    if (style != JoyvieButtonStyle.Primary) return Color.Transparent
    return if (isClickable) colors.primary else colors.primary.copy(alpha = 0.38f)
}

private fun getButtonContentColor(
    style: JoyvieButtonStyle,
    isClickable: Boolean,
    colors: JoyvieColors,
): Color {
    if (style == JoyvieButtonStyle.Primary) {
        return if (isClickable) colors.onPrimary else colors.onPrimary.copy(alpha = 0.5f)
    }
    return if (isClickable) colors.secondary else colors.secondary.copy(alpha = 0.38f)
}

private fun getButtonBorder(
    style: JoyvieButtonStyle,
    isClickable: Boolean,
    colors: JoyvieColors,
): BorderStroke? {
    if (style != JoyvieButtonStyle.Secondary) return null
    val color = if (isClickable) colors.secondary else colors.secondary.copy(alpha = 0.38f)
    return BorderStroke(1.5.dp, color)
}

@Preview
@Composable
private fun JoyvieButtonPreview(
    @PreviewParameter(ThemePreviewProvider::class) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        Surface(color = JoyvieTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                JoyvieButton(
                    text = "Primary Button",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                )
                JoyvieButton(
                    text = "Secondary Button",
                    onClick = {},
                    style = JoyvieButtonStyle.Secondary,
                    modifier = Modifier.fillMaxWidth(),
                )
                JoyvieButton(
                    text = "Loading Button",
                    onClick = {},
                    loading = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                JoyvieButton(
                    text = "Disabled Button",
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
