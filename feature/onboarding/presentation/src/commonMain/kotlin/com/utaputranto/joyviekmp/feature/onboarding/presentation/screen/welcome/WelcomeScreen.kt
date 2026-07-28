package com.utaputranto.joyviekmp.feature.onboarding.presentation.screen.welcome

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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieButton
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieLogoIcon
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyviePageIndicator
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider
import com.utaputranto.joyviekmp.feature.onboarding.domain.model.OnboardingPage
import com.utaputranto.joyviekmp.feature.onboarding.presentation.OnboardingEvent
import com.utaputranto.joyviekmp.feature.onboarding.presentation.OnboardingState
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
import org.jetbrains.compose.resources.stringResource

/**
 * Pure stateless WelcomeScreen UI composable strictly matching Joyvie Figma specs.
 *
 * Renders top header bar, horizontal carousel pager for onboarding steps,
 * indicator dots, and dynamic action button ("Next" or "Get Started").
 *
 * @param modifier [Modifier] applied to the root layout container.
 * @param state Immutable [OnboardingState] containing onboarding UI state.
 * @param pagerState State of the horizontal pager managing active carousel page.
 * @param onEvent Callback to dispatch user intents ([OnboardingEvent]) to the StateMachine.
 * @param onNext Callback to advance the horizontal pager to the next page.
 */
@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    state: OnboardingState,
    pagerState: PagerState,
    onEvent: (OnboardingEvent) -> Unit,
    onNext: () -> Unit,
) {
    val pages = state.pages
    val isLastPage = pagerState.currentPage == pages.size - 1

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
                    .padding(vertical = JoyvieTheme.dimens.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = JoyvieTheme.dimens.spacing.medium, vertical = JoyvieTheme.dimens.spacing.small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    JoyvieLogoIcon(
                        size = JoyvieTheme.dimens.icon.brandSmall,
                    )
                    Spacer(modifier = Modifier.width(JoyvieTheme.dimens.spacing.small))
                    Text(
                        text = stringResource(Res.string.onboarding_app_title),
                        style = JoyvieTheme.typography.title,
                        fontWeight = FontWeight.Bold,
                        color = JoyvieTheme.colors.onBackground,
                    )
                }

                if (!isLastPage) {
                    Text(
                        text = stringResource(Res.string.onboarding_skip),
                        style = JoyvieTheme.typography.label,
                        color = JoyvieTheme.colors.onSurfaceVariant,
                        modifier = Modifier.clickable { onEvent(OnboardingEvent.FinishOnboarding) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(JoyvieTheme.dimens.spacing.small))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
            ) { pageIndex ->
                pages.getOrNull(pageIndex)?.let { page ->
                    PagerScreen(page = page, index = pageIndex)
                }
            }

            JoyviePageIndicator(
                pageCount = pages.size,
                currentPage = pagerState.currentPage,
            )

            Spacer(modifier = Modifier.height(JoyvieTheme.dimens.spacing.medium))

            val currentPageModel = pages.getOrNull(pagerState.currentPage)
            val buttonText = currentPageModel?.buttonText?.let { stringResource(it) } ?: ""

            JoyvieButton(
                text = buttonText,
                onClick = {
                    if (isLastPage) {
                        onEvent(OnboardingEvent.FinishOnboarding)
                    } else {
                        onNext()
                    }
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = JoyvieTheme.dimens.spacing.large),
            )

            Spacer(modifier = Modifier.height(JoyvieTheme.dimens.spacing.medium))
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview(
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

    val state = OnboardingState(pages = samplePages)
    val pagerState = rememberPagerState(pageCount = { samplePages.size })

    JoyvieTheme(darkTheme = isDark) {
        WelcomeScreen(
            state = state,
            pagerState = pagerState,
            onEvent = {},
            onNext = {},
        )
    }
}
