package com.utaputranto.joyviekmp.core.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val InterFontFamily: FontFamily = FontFamily.SansSerif

@Stable
data class JoyvieTypography(
    val display: TextStyle,
    val headline: TextStyle,
    val title: TextStyle,
    val body: TextStyle,
    val label: TextStyle,
    val caption: TextStyle,
)

fun createJoyvieTypography(fontFamily: FontFamily = InterFontFamily): JoyvieTypography {
    return JoyvieTypography(
        display =
            TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 40.sp,
            ),
        headline =
            TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
            ),
        title =
            TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 28.sp,
            ),
        body =
            TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        label =
            TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 18.sp,
            ),
        caption =
            TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
    )
}
