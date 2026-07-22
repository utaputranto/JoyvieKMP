package com.utaputranto.joyviekmp.core.designsystem.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieButton
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieButtonStyle
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieChip
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyviePageIndicator
import com.utaputranto.joyviekmp.core.designsystem.molecule.JoyviePager
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

/**
 * Interactive Design System Spec Showcase for Joyvie.
 * Allows testing and previewing typography, colors, page indicator, button styles, chips, and pager.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JoyvieDesignSystemPreview(modifier: Modifier = Modifier) {
    var selectedIndicatorIndex by remember { mutableStateOf(0) }
    var selectedChipIndex by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = JoyvieTheme.colors.background,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Header Title
            Text(
                text = "Joyvie Design System Spec",
                style = JoyvieTheme.typography.display,
                color = JoyvieTheme.colors.primary,
            )
            Text(
                text = "Atomic Design System Specification with Inter Typography, #82E1FF Palette, Page Indicator, Buttons & Chips.",
                style = JoyvieTheme.typography.body,
                color = JoyvieTheme.colors.onSurfaceVariant,
            )

            // Section 1: Color Palette
            SectionHeader(title = "1. Color Palette Tokens")
            val paletteScrollState = rememberScrollState()
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(paletteScrollState),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ColorSwatch(name = "Primary\n#82E1FF", color = JoyvieTheme.colors.primary)
                ColorSwatch(name = "Inactive\n#475569", color = JoyvieTheme.colors.outline)
                ColorSwatch(name = "Background\n#0B1520", color = JoyvieTheme.colors.background)
                ColorSwatch(name = "Surface\n#122031", color = JoyvieTheme.colors.surfaceVariant)
                ColorSwatch(name = "Card Surface\n#16283D", color = Color(0xFF16283D))
            }

            // Section 2: Inter Typography
            SectionHeader(title = "2. Inter Typography Scale")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Display — 32sp Bold",
                    style = JoyvieTheme.typography.display,
                    color = JoyvieTheme.colors.onBackground,
                )
                Text(
                    "Headline — 24sp SemiBold",
                    style = JoyvieTheme.typography.headline,
                    color = JoyvieTheme.colors.onBackground,
                )
                Text(
                    "Title — 20sp Bold",
                    style = JoyvieTheme.typography.title,
                    color = JoyvieTheme.colors.onBackground,
                )
                Text(
                    "Body — 14sp Regular",
                    style = JoyvieTheme.typography.body,
                    color = JoyvieTheme.colors.onBackground,
                )
                Text(
                    "Label — 14sp SemiBold",
                    style = JoyvieTheme.typography.label,
                    color = JoyvieTheme.colors.onBackground,
                )
                Text(
                    "Caption — 12sp Medium",
                    style = JoyvieTheme.typography.caption,
                    color = JoyvieTheme.colors.onBackground,
                )
            }

            // Section 3: Buttons
            SectionHeader(title = "3. JoyvieButton Components")
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                JoyvieButton(
                    text = "Next",
                    onClick = {},
                    trailingIcon = {
                        Text(
                            "→",
                            color = JoyvieTheme.colors.onPrimary,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                JoyvieButton(
                    text = "Get Started",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                )
                JoyvieButton(
                    text = "Secondary Action",
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
            }

            // Section 4: Page Indicator
            SectionHeader(title = "4. JoyviePageIndicator Atom")
            Text(
                text = "Active: 24.dp x 6.dp (#82E1FF) | Inactive: 6.dp x 6.dp (#475569)",
                style = JoyvieTheme.typography.caption,
                color = JoyvieTheme.colors.onSurfaceVariant,
            )

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(JoyvieTheme.colors.surfaceVariant, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    JoyviePageIndicator(
                        pageCount = 3,
                        currentPage = selectedIndicatorIndex,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (0..2).forEach { idx ->
                            JoyvieChip(
                                text = "Page ${idx + 1}",
                                selected = selectedIndicatorIndex == idx,
                                onClick = { selectedIndicatorIndex = idx },
                            )
                        }
                    }
                }
            }

            // Section 5: Chips & Tags
            SectionHeader(title = "5. JoyvieChip Filter & Status Tags")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf("Action", "Sci-Fi", "Drama", "Animation").forEachIndexed { idx, label ->
                    JoyvieChip(
                        text = label,
                        selected = selectedChipIndex == idx,
                        onClick = { selectedChipIndex = idx },
                    )
                }
                JoyvieChip(
                    text = "★ 8.0+",
                    selected = true,
                    selectedContainerColor = JoyvieTheme.colors.warning.copy(alpha = 0.2f),
                    selectedContentColor = JoyvieTheme.colors.warning,
                )
                JoyvieChip(
                    text = "Added to Library",
                    selected = true,
                )
            }

            // Section 6: JoyviePager Molecule
            SectionHeader(title = "6. JoyviePager Interactive Molecule")
            val pagerState = rememberPagerState(pageCount = { 3 })
            Card(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                colors = CardDefaults.cardColors(containerColor = JoyvieTheme.colors.surfaceVariant),
                shape = RoundedCornerShape(16.dp),
            ) {
                JoyviePager(
                    pageCount = 3,
                    pagerState = pagerState,
                    modifier = Modifier.padding(16.dp),
                ) { pageIndex ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Interactive Slide Page ${pageIndex + 1}",
                            style = JoyvieTheme.typography.title,
                            color = JoyvieTheme.colors.primary,
                        )
                    }
                }
            }

            // Section 7: JoyvieSearchBar Atom
            SectionHeader(title = "7. JoyvieSearchBar Atom")
            var searchQuery by remember { mutableStateOf("") }
            com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Search movies, actors, genres...",
            )

            // Section 8: JoyvieOverlay Legibility Gradient
            SectionHeader(title = "8. JoyvieOverlay Legibility Gradient")
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1E3A5A)),
            ) {
                com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieOverlay()
                Text(
                    text = "Text overlay for legibility",
                    style = JoyvieTheme.typography.title,
                    color = JoyvieTheme.colors.onBackground,
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = JoyvieTheme.typography.title,
        color = JoyvieTheme.colors.primary,
    )
}

@Composable
private fun ColorSwatch(
    name: String,
    color: Color,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color)
                    .border(1.dp, JoyvieTheme.colors.outlineVariant, RoundedCornerShape(12.dp)),
        )
        Text(
            text = name,
            style = JoyvieTheme.typography.caption,
            color = JoyvieTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
private fun JoyvieDesignSystemPreviewSpec(
    @androidx.compose.ui.tooling.preview.PreviewParameter(
        com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider::class,
    ) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        JoyvieDesignSystemPreview()
    }
}
