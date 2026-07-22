package com.utaputranto.joyviekmp.core.designsystem.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider

/**
 * Search component built strictly according to Joyvie Figma spec.
 *
 * @param query Current text query in search bar.
 * @param onQueryChange Callback invoked when search text changes.
 * @param placeholder Placeholder text when search bar is empty (default "Search movies, actors, genres...").
 * @param onSearch Callback invoked when search action button is pressed on soft keyboard.
 * @param onClear Callback invoked when clear button (✕) is clicked.
 */
@Composable
fun JoyvieSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search movies, actors, genres...",
    onSearch: ((String) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(16.dp)
    val containerColor = Color(0xFF16283D)
    val borderColor = Color(0xFF273B54)

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(shape)
                .background(containerColor)
                .border(1.dp, borderColor, shape)
                .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Search Icon
            Text(
                text = "🔍",
                style = JoyvieTheme.typography.body,
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Text Input / Placeholder
            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = JoyvieTheme.typography.body,
                        color = Color(0xFF8A9AAE),
                    )
                }

                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    enabled = enabled,
                    textStyle =
                        JoyvieTheme.typography.body.copy(
                            color = JoyvieTheme.colors.onBackground,
                        ),
                    cursorBrush = SolidColor(JoyvieTheme.colors.primary),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions =
                        KeyboardActions(
                            onSearch = { onSearch?.invoke(query) },
                        ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Clear Button (✕)
            if (query.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier =
                        Modifier
                            .size(20.dp)
                            .clickable {
                                onQueryChange("")
                                onClear?.invoke()
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "✕",
                        style = JoyvieTheme.typography.caption,
                        color = Color(0xFF8A9AAE),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun JoyvieSearchBarPreview(
    @PreviewParameter(ThemePreviewProvider::class) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        Surface(color = JoyvieTheme.colors.background) {
            Box(modifier = Modifier.padding(16.dp)) {
                JoyvieSearchBar(
                    query = "",
                    onQueryChange = {},
                    placeholder = "Search movies, actors, genres...",
                )
            }
        }
    }
}
