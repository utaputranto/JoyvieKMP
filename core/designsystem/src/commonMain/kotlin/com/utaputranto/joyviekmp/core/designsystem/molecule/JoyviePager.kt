package com.utaputranto.joyviekmp.core.designsystem.molecule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyviePageIndicator

/**
 * Reusable JoyviePager molecule combining Compose HorizontalPager and JoyviePageIndicator.
 */
@Composable
fun JoyviePager(
    pageCount: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    showIndicator: Boolean = true,
    indicatorModifier: Modifier = Modifier,
    pageContent: @Composable (page: Int) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
        ) { page ->
            pageContent(page)
        }

        if (showIndicator) {
            JoyviePageIndicator(
                pageCount = pageCount,
                currentPage = pagerState.currentPage,
                modifier = indicatorModifier,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
private fun JoyviePagerPreview(
    @androidx.compose.ui.tooling.preview.PreviewParameter(
        com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider::class,
    ) isDark: Boolean,
) {
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(pageCount = { 3 })
    com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme(darkTheme = isDark) {
        Surface(
            color = com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme.colors.background,
        ) {
            JoyviePager(
                pageCount = 3,
                pagerState = pagerState,
                modifier = Modifier.height(200.dp),
            ) { page ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Page $page",
                        color = com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme.colors.onBackground,
                    )
                }
            }
        }
    }
}
