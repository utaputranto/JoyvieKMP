package com.utaputranto.joyviekmp.core.designsystem.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

/**
 * Filter chip & status tag atom component for Joyvie Design System.
 * Used for movie categories (Action, Sci-Fi), ratings (★ 8.0+), and library status badges.
 */
@Composable
fun JoyvieChip(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    selectedContainerColor: Color = JoyvieTheme.colors.primary,
    selectedContentColor: Color = JoyvieTheme.colors.onPrimary,
    unselectedContainerColor: Color = JoyvieTheme.colors.surfaceVariant,
    unselectedContentColor: Color = JoyvieTheme.colors.onSurfaceVariant,
) {
    val containerColor = if (selected) selectedContainerColor else unselectedContainerColor
    val contentColor = if (selected) selectedContentColor else unselectedContentColor
    val shape = RoundedCornerShape(16.dp)

    val clickableModifier =
        if (onClick != null) {
            Modifier.clickable(onClick = onClick)
        } else {
            Modifier
        }

    Box(
        modifier =
            modifier
                .clip(shape)
                .background(containerColor)
                .then(clickableModifier)
                .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (leadingIcon != null) {
                Box(modifier = Modifier.size(14.dp)) {
                    leadingIcon()
                }
            }

            Text(
                text = text,
                style = JoyvieTheme.typography.caption,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = contentColor,
            )
        }
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
private fun JoyvieChipPreview(
    @androidx.compose.ui.tooling.preview.PreviewParameter(
        com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider::class,
    ) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        androidx.compose.material3.Surface(
            color = JoyvieTheme.colors.background,
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                JoyvieChip(text = "Action", selected = true)
                JoyvieChip(text = "Sci-Fi", selected = false)
            }
        }
    }
}
