package com.utaputranto.joyviekmp.core.designsystem.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyviePosterCard
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme

data class MovieSectionItem(
    val id: Int,
    val imageUrl: String,
    val title: String,
)

/**
 * Horizontal movie list molecule for the Joyvie Design System.
 * Renders loading/error/content states for one titled section (e.g. "Now Playing").
 */
@Composable
fun JoyvieMovieSection(
    title: String,
    movies: List<MovieSectionItem>,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onMovieClick: (MovieSectionItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = JoyvieTheme.typography.title,
            color = JoyvieTheme.colors.onBackground,
            modifier = Modifier.padding(horizontal = JoyvieTheme.dimens.spacing.medium),
        )
        Spacer(JoyvieTheme.dimens.spacing.small)
        when {
            isLoading ->
                CircularProgressIndicator(
                    modifier = Modifier.padding(horizontal = JoyvieTheme.dimens.spacing.medium),
                    color = JoyvieTheme.colors.primary,
                )

            errorMessage != null ->
                Row(modifier = Modifier.padding(horizontal = JoyvieTheme.dimens.spacing.medium)) {
                    Text(
                        text = errorMessage,
                        style = JoyvieTheme.typography.body,
                        color = JoyvieTheme.colors.error,
                    )
                    TextButton(onClick = onRetry) {
                        Text(text = "Retry", style = JoyvieTheme.typography.body)
                    }
                }

            else ->
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(JoyvieTheme.dimens.spacing.small),
                    contentPadding =
                        androidx.compose.foundation.layout.PaddingValues(
                            horizontal = JoyvieTheme.dimens.spacing.medium,
                        ),
                ) {
                    items(movies, key = { it.id }) { movie ->
                        JoyviePosterCard(
                            imageUrl = movie.imageUrl,
                            contentDescription = movie.title,
                            onClick = { onMovieClick(movie) },
                        )
                    }
                }
        }
    }
}

@Composable
private fun Spacer(height: androidx.compose.ui.unit.Dp) {
    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = height))
}

@Composable
@androidx.compose.ui.tooling.preview.Preview
private fun JoyvieMovieSectionPreview(
    @androidx.compose.ui.tooling.preview.PreviewParameter(
        com.utaputranto.joyviekmp.core.designsystem.theme.ThemePreviewProvider::class,
    ) isDark: Boolean,
) {
    JoyvieTheme(darkTheme = isDark) {
        JoyvieMovieSection(
            title = "Now Playing",
            movies =
                listOf(
                    MovieSectionItem(1, "https://example.com/poster1.jpg", "Movie 1"),
                    MovieSectionItem(2, "https://example.com/poster2.jpg", "Movie 2"),
                ),
            isLoading = false,
            errorMessage = null,
            onRetry = {},
            onMovieClick = {},
        )
    }
}
