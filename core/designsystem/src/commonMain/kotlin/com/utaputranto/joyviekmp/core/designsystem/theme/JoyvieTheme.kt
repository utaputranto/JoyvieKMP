package com.utaputranto.joyviekmp.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalJoyvieColors =
    staticCompositionLocalOf<JoyvieColors> {
        error("No JoyvieColors provided")
    }

val LocalJoyvieDimens =
    staticCompositionLocalOf<JoyvieDimens> {
        error("No JoyvieDimens provided")
    }

val LocalJoyvieTypography =
    staticCompositionLocalOf<JoyvieTypography> {
        error("No JoyvieTypography provided")
    }

object JoyvieTheme {
    val colors: JoyvieColors
        @Composable
        @ReadOnlyComposable
        get() = LocalJoyvieColors.current

    val dimens: JoyvieDimens
        @Composable
        @ReadOnlyComposable
        get() = LocalJoyvieDimens.current

    val typography: JoyvieTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalJoyvieTypography.current
}

@Composable
fun JoyvieTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkJoyvieColors else LightJoyvieColors
    val dimens = JoyvieDimens()
    val typography = createJoyvieTypography()

    val materialColorScheme =
        if (darkTheme) {
            darkColorScheme(
                primary = colors.primary,
                onPrimary = colors.onPrimary,
                primaryContainer = colors.primaryContainer,
                onPrimaryContainer = colors.onPrimaryContainer,
                secondary = colors.secondary,
                onSecondary = colors.onSecondary,
                secondaryContainer = colors.secondaryContainer,
                onSecondaryContainer = colors.onSecondaryContainer,
                background = colors.background,
                onBackground = colors.onBackground,
                surface = colors.surface,
                onSurface = colors.onSurface,
                surfaceVariant = colors.surfaceVariant,
                onSurfaceVariant = colors.onSurfaceVariant,
                outline = colors.outline,
                outlineVariant = colors.outlineVariant,
                error = colors.error,
                onError = colors.onError,
                errorContainer = colors.errorContainer,
                onErrorContainer = colors.onErrorContainer,
            )
        } else {
            lightColorScheme(
                primary = colors.primary,
                onPrimary = colors.onPrimary,
                primaryContainer = colors.primaryContainer,
                onPrimaryContainer = colors.onPrimaryContainer,
                secondary = colors.secondary,
                onSecondary = colors.onSecondary,
                secondaryContainer = colors.secondaryContainer,
                onSecondaryContainer = colors.onSecondaryContainer,
                background = colors.background,
                onBackground = colors.onBackground,
                surface = colors.surface,
                onSurface = colors.onSurface,
                surfaceVariant = colors.surfaceVariant,
                onSurfaceVariant = colors.onSurfaceVariant,
                outline = colors.outline,
                outlineVariant = colors.outlineVariant,
                error = colors.error,
                onError = colors.onError,
                errorContainer = colors.errorContainer,
                onErrorContainer = colors.onErrorContainer,
            )
        }

    val materialTypography =
        Typography(
            displayLarge = typography.display,
            displayMedium = typography.display,
            displaySmall = typography.display,
            headlineLarge = typography.headline,
            headlineMedium = typography.headline,
            headlineSmall = typography.headline,
            titleLarge = typography.title,
            titleMedium = typography.title,
            titleSmall = typography.title,
            bodyLarge = typography.body,
            bodyMedium = typography.body,
            bodySmall = typography.body,
            labelLarge = typography.label,
            labelMedium = typography.label,
            labelSmall = typography.label,
        )

    CompositionLocalProvider(
        LocalJoyvieColors provides colors,
        LocalJoyvieDimens provides dimens,
        LocalJoyvieTypography provides typography,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = materialTypography,
            content = content,
        )
    }
}
