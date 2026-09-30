package com.utaputranto.joyviekmp.core.designsystem.atom

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider

/**
 * Brand logo icon matching exact Joyvie Figma design specifications:
 * Cyan rectangle box containing 3 dark slanted film-strip parallelograms (///).
 */
@Composable
fun JoyvieLogoIcon(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    containerColor: Color = JoyvieTheme.colors.primary,
    stripColor: Color = Color(0xFF060D15),
) {
    Box(
        modifier =
            modifier
                .size(size)
                .clip(RoundedCornerShape(size * 0.15f))
                .background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(width = size * 0.85f, height = size * 0.45f)) {
            val width = this.size.width
            val height = this.size.height

            val slantOffset = width * 0.18f
            val stripWidth = width * 0.20f
            val spacing = width * 0.08f

            val startY = 0f
            val endY = height

            for (i in 0..2) {
                val startX = (i * (stripWidth + spacing)) + (width * 0.04f)
                val path =
                    Path().apply {
                        moveTo(startX + slantOffset, startY)
                        lineTo(startX + slantOffset + stripWidth, startY)
                        lineTo(startX + stripWidth, endY)
                        lineTo(startX, endY)
                        close()
                    }
                drawPath(path = path, color = stripColor)
            }
        }
    }
}

@Preview
@Composable
private fun JoyvieLogoIconPreview(
    @PreviewParameter(ThemePreviewProvider::class) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        Surface(color = JoyvieTheme.colors.background) {
            Box(modifier = Modifier.padding(16.dp)) {
                JoyvieLogoIcon()
            }
        }
    }
}
