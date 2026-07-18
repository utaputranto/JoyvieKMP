package com.utaputranto.joyviekmp.core.designsystem.atom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieColors
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider

@Composable
fun JoyvieTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            textStyle = JoyvieTheme.typography.body,
            label = {
                Text(
                    text = label,
                    style = JoyvieTheme.typography.label,
                )
            },
            placeholder =
                placeholder?.let {
                    {
                        Text(
                            text = it,
                            style = JoyvieTheme.typography.body,
                        )
                    }
                },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(12.dp),
            colors = getJoyvieTextFieldColors(JoyvieTheme.colors),
        )
        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = JoyvieTheme.colors.error,
                style = JoyvieTheme.typography.label,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp),
            )
        }
    }
}

@Composable
private fun getJoyvieTextFieldColors(colors: JoyvieColors): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = colors.onBackground,
        unfocusedTextColor = colors.onBackground,
        disabledTextColor = colors.onBackground.copy(alpha = 0.38f),
        errorTextColor = colors.error,
        focusedBorderColor = colors.primary,
        unfocusedBorderColor = colors.secondary.copy(alpha = 0.4f),
        disabledBorderColor = colors.secondary.copy(alpha = 0.12f),
        errorBorderColor = colors.error,
        focusedLabelColor = colors.primary,
        unfocusedLabelColor = colors.secondary.copy(alpha = 0.6f),
        disabledLabelColor = colors.secondary.copy(alpha = 0.38f),
        errorLabelColor = colors.error,
        focusedLeadingIconColor = colors.primary,
        unfocusedLeadingIconColor = colors.secondary.copy(alpha = 0.6f),
        disabledLeadingIconColor = colors.secondary.copy(alpha = 0.38f),
        errorLeadingIconColor = colors.error,
        focusedTrailingIconColor = colors.primary,
        unfocusedTrailingIconColor = colors.secondary.copy(alpha = 0.6f),
        disabledTrailingIconColor = colors.secondary.copy(alpha = 0.38f),
        errorTrailingIconColor = colors.error,
    )
}

@Preview
@Composable
private fun JoyvieTextFieldPreview(
    @PreviewParameter(ThemePreviewProvider::class) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        Surface(color = JoyvieTheme.colors.background) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                JoyvieTextField(
                    value = "Input text",
                    onValueChange = {},
                    label = "Default State",
                )
                JoyvieTextField(
                    value = "",
                    onValueChange = {},
                    label = "With Placeholder",
                    placeholder = "Enter text...",
                )
                JoyvieTextField(
                    value = "Error input",
                    onValueChange = {},
                    label = "Error State",
                    isError = true,
                    errorMessage = "This field is required.",
                )
                JoyvieTextField(
                    value = "Disabled text",
                    onValueChange = {},
                    label = "Disabled State",
                    enabled = false,
                )
            }
        }
    }
}
