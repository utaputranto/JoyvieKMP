package com.utaputranto.joyviekmp.core.designsystem.atom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

/**
 * Bottom navigation bar atom for the Joyvie Design System.
 * Generic — the caller supplies its own tab items, so it's reusable across features.
 */
data class JoyvieBottomNavItem(
    val label: String,
    val icon: (@Composable () -> Unit)? = null,
)

@Composable
fun JoyvieBottomNavBar(
    items: List<JoyvieBottomNavItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = JoyvieTheme.colors.surface,
        contentColor = JoyvieTheme.colors.onSurface,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = { onSelect(index) },
                icon = { item.icon?.invoke() },
                label = { Text(text = item.label, style = JoyvieTheme.typography.caption) },
                colors =
                    androidx.compose.material3.NavigationBarItemDefaults.colors(
                        selectedIconColor = JoyvieTheme.colors.primary,
                        selectedTextColor = JoyvieTheme.colors.primary,
                        unselectedIconColor = JoyvieTheme.colors.onSurfaceVariant,
                        unselectedTextColor = JoyvieTheme.colors.onSurfaceVariant,
                    ),
            )
        }
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
private fun JoyvieBottomNavBarPreview(
    @androidx.compose.ui.tooling.preview.PreviewParameter(
        com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider::class,
    ) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        Column {
            JoyvieBottomNavBar(
                items =
                    listOf(
                        JoyvieBottomNavItem("Home"),
                        JoyvieBottomNavItem("Search"),
                        JoyvieBottomNavItem("Profile"),
                    ),
                selectedIndex = 0,
                onSelect = {},
                modifier = Modifier.padding(JoyvieTheme.dimens.spacing.none),
            )
        }
    }
}
