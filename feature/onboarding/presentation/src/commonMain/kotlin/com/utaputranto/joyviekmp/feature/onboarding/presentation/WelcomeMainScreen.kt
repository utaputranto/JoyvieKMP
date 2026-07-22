package com.utaputranto.joyviekmp.feature.onboarding.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieButton
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieLogoIcon
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyviePageIndicator
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider
import com.utaputranto.joyviekmp.feature.onboarding.domain.model.OnboardingPage
import joyviekmp.feature.onboarding.presentation.generated.resources.Res
import joyviekmp.feature.onboarding.presentation.generated.resources.ic_onboarding_step1_trending
import joyviekmp.feature.onboarding.presentation.generated.resources.ic_onboarding_step2_search
import joyviekmp.feature.onboarding.presentation.generated.resources.ic_onboarding_step3_watchlist
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_app_title
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_button_get_started
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_button_next
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_skip
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_step1_description
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_step1_title
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_step1_title_highlight
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_step2_description
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_step2_title
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_step2_title_highlight
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_step3_description
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_step3_title
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_step3_title_highlight
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * WelcomeMainScreen composable container strictly matching Joyvie Figma specs.
 *
 * @param pages List of onboarding pages loaded from repository.
 * @param onSkip Callback when user clicks top-right SKIP button.
 * @param onFinish Callback when user finishes onboarding (clicks Get Started on final page).
 */
@Composable
fun WelcomeMainScreen(
    pages: List<OnboardingPage>,
    onSkip: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.size - 1

    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = pagerState.currentPage > 0,
        onBackCompleted = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        },
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = JoyvieTheme.colors.background,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Header Top Bar Section
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Joyvie Brand Logo & Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    JoyvieLogoIcon(
                        size = 28.dp,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.onboarding_app_title),
                        style = JoyvieTheme.typography.title,
                        fontWeight = FontWeight.Bold,
                        color = JoyvieTheme.colors.onBackground,
                    )
                }

                // SKIP Action Text (Hidden on the final step)
                if (!isLastPage) {
                    Text(
                        text = stringResource(Res.string.onboarding_skip),
                        style = JoyvieTheme.typography.label,
                        color = JoyvieTheme.colors.onSurfaceVariant,
                        modifier = Modifier.clickable(onClick = onSkip),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Carousel Horizontal Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
            ) { pageIndex ->
                pages.getOrNull(pageIndex)?.let { page ->
                    PagerScreen(page = page, index = pageIndex)
                }
            }

            // Indicator Dots
            JoyviePageIndicator(
                pageCount = pages.size,
                currentPage = pagerState.currentPage,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Action Button
            val currentPageModel = pages.getOrNull(pagerState.currentPage)
            val buttonText = currentPageModel?.buttonText?.let { stringResource(it) } ?: ""

            JoyvieButton(
                text = buttonText,
                onClick = {
                    if (isLastPage) {
                        onFinish()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun WelcomeMainScreenPreview(
    @PreviewParameter(ThemePreviewProvider::class) isDark: Boolean,
) {
    val samplePages =
        listOf(
            OnboardingPage(
                id = 1,
                title = Res.string.onboarding_step1_title,
                titleHighlight = Res.string.onboarding_step1_title_highlight,
                description = Res.string.onboarding_step1_description,
                buttonText = Res.string.onboarding_button_next,
                imageBackground = Res.drawable.ic_onboarding_step1_trending,
            ),
            OnboardingPage(
                id = 2,
                title = Res.string.onboarding_step2_title,
                titleHighlight = Res.string.onboarding_step2_title_highlight,
                description = Res.string.onboarding_step2_description,
                buttonText = Res.string.onboarding_button_next,
                imageBackground = Res.drawable.ic_onboarding_step2_search,
            ),
            OnboardingPage(
                id = 3,
                title = Res.string.onboarding_step3_title,
                titleHighlight = Res.string.onboarding_step3_title_highlight,
                description = Res.string.onboarding_step3_description,
                buttonText = Res.string.onboarding_button_get_started,
                imageBackground = Res.drawable.ic_onboarding_step3_watchlist,
            ),
        )

    JoyvieTheme(darkTheme = isDark) {
        WelcomeMainScreen(
            pages = samplePages,
            onSkip = {},
            onFinish = {},
        )
    }
}
