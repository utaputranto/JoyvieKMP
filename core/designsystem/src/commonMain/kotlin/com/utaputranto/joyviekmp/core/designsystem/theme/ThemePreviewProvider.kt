package com.utaputranto.joyviekmp.core.designsystem.theme

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ThemePreviewProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(false, true)
}
