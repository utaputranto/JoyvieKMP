package com.utaputranto.joyviekmp.feature.onboarding.presentation.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieOverlay
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider
import com.utaputranto.joyviekmp.feature.onboarding.domain.model.OnboardingPage
import joyviekmp.feature.onboarding.presentation.generated.resources.Res
import joyviekmp.feature.onboarding.presentation.generated.resources.ic_onboarding_step1_trending
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_button_next
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_step1_description
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_step1_title
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_step1_title_highlight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Individual onboarding slide carousel item rendering the hero illustration, title, highlight, and description.
 *
 * @param modifier [Modifier] applied to the root column container.
 * @param page Data model containing titles, description, and image resources for this slide.
 * @param index Zero-based carousel index used to determine content scaling logic.
 */
@Composable
fun PagerScreen(
    modifier: Modifier = Modifier,
    page: OnboardingPage,
    index: Int = 0,
) {
    val isCrop = index == 0

    val titleText = stringResource(page.title)
    val titleHighlightText = stringResource(page.titleHighlight)
    val descriptionText = stringResource(page.description)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1.3f),
            contentAlignment = Alignment.TopCenter,
        ) {
            Image(
                painter = painterResource(page.imageBackground),
                contentDescription = "$titleText $titleHighlightText",
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = JoyvieTheme.dimens.spacing.medium,
                            vertical = JoyvieTheme.dimens.spacing.large,
                        ),
                alignment = Alignment.TopCenter,
                contentScale =
                    if (isCrop) {
                        ContentScale.Crop
                    } else {
                        ContentScale.Fit
                    },
            )

            JoyvieOverlay()
        }

        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.spacing.medium))

        Text(
            text = titleText,
            style = JoyvieTheme.typography.title.copy(fontSize = 24.sp),
            color = JoyvieTheme.colors.onBackground,
            textAlign = TextAlign.Center,
        )
        Text(
            text = titleHighlightText,
            style = JoyvieTheme.typography.title.copy(fontSize = 24.sp),
            color = JoyvieTheme.colors.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.spacing.mediumSmall))

        Text(
            text = descriptionText,
            style = JoyvieTheme.typography.body,
            color = JoyvieTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = JoyvieTheme.dimens.spacing.medium),
        )

        Spacer(modifier = Modifier.height(JoyvieTheme.dimens.spacing.medium))
    }
}

@Preview
@Composable
private fun PagerScreenPreview(
    @PreviewParameter(ThemePreviewProvider::class) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        Surface(color = JoyvieTheme.colors.background) {
            PagerScreen(
                page =
                    OnboardingPage(
                        id = 1,
                        title = Res.string.onboarding_step1_title,
                        titleHighlight = Res.string.onboarding_step1_title_highlight,
                        description = Res.string.onboarding_step1_description,
                        buttonText = Res.string.onboarding_button_next,
                        imageBackground = Res.drawable.ic_onboarding_step1_trending,
                    ),
            )
        }
    }
}
