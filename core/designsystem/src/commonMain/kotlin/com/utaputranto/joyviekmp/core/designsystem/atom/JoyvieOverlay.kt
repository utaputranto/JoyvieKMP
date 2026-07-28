package com.utaputranto.joyviekmp.core.designsystem.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider

/**
 * Legibility gradient overlay component built strictly according to Joyvie Figma spec.
 * Provides a vertical gradient overlay transition from transparent top to dark navy background for text legibility.
 *
 * @param topColor Color at the top of the gradient (default Transparent).
 * @param bottomColor Color at the bottom of the gradient (default #0B1520 / JoyvieTheme.colors.background).
 */
@Composable
fun JoyvieOverlay(
    modifier: Modifier = Modifier,
    topColor: Color = Color.Transparent,
    midColor: Color = JoyvieTheme.colors.background.copy(alpha = 0.6f),
    bottomColor: Color = JoyvieTheme.colors.background,
) {
    val brush =
        Brush.verticalGradient(
            colors = listOf(topColor, midColor, bottomColor),
        )

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(brush),
    )
}

/**
 * Modifier extension to apply Joyvie legibility gradient overlay to any composable container.
 */
fun Modifier.joyvieGradientOverlay(
    topColor: Color = Color.Transparent,
    midColor: Color = Color(0x990B1520),
    bottomColor: Color = Color(0xFF0B1520),
): Modifier =
    this.background(
        brush =
            Brush.verticalGradient(
                colors = listOf(topColor, midColor, bottomColor),
            ),
    )

@Preview
@Composable
private fun JoyvieOverlayPreview(
    @PreviewParameter(ThemePreviewProvider::class) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        Surface(color = JoyvieTheme.colors.background) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(240.dp),
            ) {
                // Background preview content
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(Color(0xFF1E3A5A)),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Text(
                        text = "Background Movie Poster / Banner",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp),
                    )
                }

                // Overlay
                JoyvieOverlay()

                // Foreground Legible Text
                Text(
                    text = "Discover Trending Movies",
                    style = JoyvieTheme.typography.headline,
                    color = JoyvieTheme.colors.onBackground,
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                )
            }
        }
    }
}
