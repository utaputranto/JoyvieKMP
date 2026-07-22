package com.utaputranto.joyviekmp.feature.onboarding.presentation.screen.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieLogoIcon
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieOverlay
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider
import joyviekmp.feature.onboarding.presentation.generated.resources.Res
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_app_title
import joyviekmp.feature.onboarding.presentation.generated.resources.onboarding_splash_subtitle
import org.jetbrains.compose.resources.stringResource

/**
 * SplashMainScreen composable strictly matching Joyvie Figma cold launch specs.
 *
 * @param modifier [Modifier] applied to the root container.
 */
@Composable
fun SplashMainScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = JoyvieTheme.colors.background,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
        ) {
            JoyvieOverlay()

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                JoyvieLogoIcon(
                    size = JoyvieTheme.dimens.icon.brandLarge,
                )

                Spacer(modifier = Modifier.height(JoyvieTheme.dimens.spacing.medium))

                Text(
                    text = stringResource(Res.string.onboarding_app_title),
                    style =
                        JoyvieTheme.typography.display.copy(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    color = JoyvieTheme.colors.onBackground,
                )
            }

            Text(
                text = stringResource(Res.string.onboarding_splash_subtitle),
                style = JoyvieTheme.typography.caption,
                color = JoyvieTheme.colors.onSurfaceVariant,
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = JoyvieTheme.dimens.spacing.xxlarge),
            )
        }
    }
}

@Preview
@Composable
private fun SplashMainScreenPreview(
    @PreviewParameter(ThemePreviewProvider::class) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        SplashMainScreen()
    }
}
