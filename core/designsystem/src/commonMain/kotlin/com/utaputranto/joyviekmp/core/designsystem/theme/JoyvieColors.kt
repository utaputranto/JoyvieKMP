package com.utaputranto.joyviekmp.core.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

/**
 * Primitive color tokens: raw ramps, never referenced directly by UI code.
 * UI code always goes through the semantic tokens in [JoyvieColors].
 */
private object JoyviePalette {
    // Brand — Ocean (cyan)
    val Ocean100 = Color(0xFFB3ECFA)
    val Ocean400 = Color(0xFF26C8EF)
    val Ocean600 = Color(0xFF019FC9)
    val Ocean900 = Color(0xFF014A5F)

    // Brand — Navy (deep blue)
    val Navy100 = Color(0xFFC2CEDA)
    val Navy300 = Color(0xFF728EA9)
    val Navy700 = Color(0xFF1E3A5A)
    val Navy800 = Color(0xFF032541)
    val Navy900 = Color(0xFF021A2E)

    // Neutral — Slate
    val Slate50 = Color(0xFFF8FAFC)
    val Slate100 = Color(0xFFF1F5F9)
    val Slate200 = Color(0xFFE2E8F0)
    val Slate300 = Color(0xFFCBD5E1)
    val Slate400 = Color(0xFF94A3B8)
    val Slate600 = Color(0xFF475569)
    val Slate700 = Color(0xFF334155)
    val Slate800 = Color(0xFF1E293B)
    val Slate900 = Color(0xFF0F172A)

    // Status — Success (green)
    val Green400 = Color(0xFF4ADE80)
    val Green600 = Color(0xFF16A34A)
    val Green950 = Color(0xFF052E16)

    // Status — Warning (amber)
    val Amber400 = Color(0xFFFBBF24)
    val Amber600 = Color(0xFFD97706)
    val Amber950 = Color(0xFF451A03)

    // Status — Error (red)
    val Red100 = Color(0xFFFEE2E2)
    val Red400 = Color(0xFFF87171)
    val Red600 = Color(0xFFDC2626)
    val Red900 = Color(0xFF7F1D1D)
    val Red950 = Color(0xFF450A0A)

    // Status — Info (blue)
    val Blue100 = Color(0xFFDBEAFE)
    val Blue400 = Color(0xFF60A5FA)
    val Blue600 = Color(0xFF2563EB)
    val Blue950 = Color(0xFF172554)

    val White = Color(0xFFFFFFFF)
}

/**
 * Semantic color tokens: named after ROLE, not hue. UI code reads these via
 * JoyvieTheme.colors so light/dark stay consistent automatically.
 */
@Stable
data class JoyvieColors(
    // Brand
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    // Backgrounds & surfaces
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val outline: Color,
    val outlineVariant: Color,
    // Status
    val success: Color,
    val onSuccess: Color,
    val warning: Color,
    val onWarning: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val info: Color,
    val onInfo: Color,
    val isLight: Boolean,
)

val LightJoyvieColors =
    JoyvieColors(
        primary = JoyviePalette.Ocean600,
        onPrimary = JoyviePalette.White,
        primaryContainer = JoyviePalette.Ocean100,
        onPrimaryContainer = JoyviePalette.Ocean900,
        secondary = JoyviePalette.Navy800,
        onSecondary = JoyviePalette.White,
        secondaryContainer = JoyviePalette.Navy100,
        onSecondaryContainer = JoyviePalette.Navy900,
        background = JoyviePalette.Slate50,
        onBackground = JoyviePalette.Slate900,
        surface = JoyviePalette.White,
        onSurface = JoyviePalette.Slate900,
        surfaceVariant = JoyviePalette.Slate100,
        onSurfaceVariant = JoyviePalette.Slate600,
        outline = JoyviePalette.Slate300,
        outlineVariant = JoyviePalette.Slate200,
        success = JoyviePalette.Green600,
        onSuccess = JoyviePalette.White,
        warning = JoyviePalette.Amber600,
        onWarning = JoyviePalette.White,
        error = JoyviePalette.Red600,
        onError = JoyviePalette.White,
        errorContainer = JoyviePalette.Red100,
        onErrorContainer = JoyviePalette.Red900,
        info = JoyviePalette.Blue600,
        onInfo = JoyviePalette.White,
        isLight = true,
    )

val DarkJoyvieColors =
    JoyvieColors(
        primary = JoyviePalette.Ocean400,
        onPrimary = JoyviePalette.Navy900,
        primaryContainer = JoyviePalette.Navy700,
        onPrimaryContainer = JoyviePalette.Ocean100,
        secondary = JoyviePalette.Navy300,
        onSecondary = JoyviePalette.Navy900,
        secondaryContainer = JoyviePalette.Navy700,
        onSecondaryContainer = JoyviePalette.Slate100,
        background = JoyviePalette.Slate900,
        onBackground = JoyviePalette.Slate100,
        surface = JoyviePalette.Slate800,
        onSurface = JoyviePalette.Slate100,
        surfaceVariant = JoyviePalette.Slate700,
        onSurfaceVariant = JoyviePalette.Slate400,
        outline = JoyviePalette.Slate600,
        outlineVariant = JoyviePalette.Slate700,
        success = JoyviePalette.Green400,
        onSuccess = JoyviePalette.Green950,
        warning = JoyviePalette.Amber400,
        onWarning = JoyviePalette.Amber950,
        error = JoyviePalette.Red400,
        onError = JoyviePalette.Red950,
        errorContainer = JoyviePalette.Red900,
        onErrorContainer = JoyviePalette.Red100,
        info = JoyviePalette.Blue400,
        onInfo = JoyviePalette.Blue950,
        isLight = false,
    )
