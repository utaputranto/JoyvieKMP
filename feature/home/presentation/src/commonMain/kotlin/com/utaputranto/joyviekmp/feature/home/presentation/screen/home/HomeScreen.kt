package com.utaputranto.joyviekmp.feature.home.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieBottomNavBar
import com.utaputranto.joyviekmp.core.designsystem.atom.JoyvieBottomNavItem
import com.utaputranto.joyviekmp.core.designsystem.molecule.JoyvieMovieSection
import com.utaputranto.joyviekmp.core.designsystem.molecule.MovieSectionItem
import com.utaputranto.joyviekmp.core.designsystem.theme.JoyvieTheme
import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.presentation.HomeEvent
import com.utaputranto.joyviekmp.feature.home.presentation.HomeState
import com.utaputranto.joyviekmp.feature.home.presentation.HomeTab
import com.utaputranto.joyviekmp.feature.home.presentation.SectionUiState
import com.utaputranto.joyviekmp.feature.home.presentation.screen.profile.ProfilePlaceholderScreen
import com.utaputranto.joyviekmp.feature.home.presentation.screen.search.SearchPlaceholderScreen

private val TABS =
    listOf(
        JoyvieBottomNavItem("Home"),
        JoyvieBottomNavItem("Search"),
        JoyvieBottomNavItem("Profile"),
    )

@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JoyvieTheme.colors.background,
        bottomBar = {
            JoyvieBottomNavBar(
                items = TABS,
                selectedIndex = state.selectedTab.ordinal,
                onSelect = { index -> onEvent(HomeEvent.SelectTab(HomeTab.entries[index])) },
            )
        },
    ) { innerPadding ->
        when (state.selectedTab) {
            HomeTab.Home ->
                HomeTabContent(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                )

            HomeTab.Search -> SearchPlaceholderScreen(modifier = Modifier.fillMaxSize().padding(innerPadding))
            HomeTab.Profile -> ProfilePlaceholderScreen(modifier = Modifier.fillMaxSize().padding(innerPadding))
        }
    }
}

@Composable
private fun HomeTabContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(JoyvieTheme.dimens.spacing.large),
    ) {
        JoyvieMovieSection(
            title = "Now Playing",
            movies = state.nowPlaying.toSectionItems(),
            isLoading = state.nowPlaying is SectionUiState.Loading,
            errorMessage = (state.nowPlaying as? SectionUiState.Error)?.message,
            onRetry = { onEvent(HomeEvent.RetryNowPlaying) },
            onMovieClick = {},
        )
        JoyvieMovieSection(
            title = "Latest",
            movies = state.latest.toSectionItems(),
            isLoading = state.latest is SectionUiState.Loading,
            errorMessage = (state.latest as? SectionUiState.Error)?.message,
            onRetry = { onEvent(HomeEvent.RetryLatest) },
            onMovieClick = {},
        )
        JoyvieMovieSection(
            title = "Upcoming",
            movies = state.upcoming.toSectionItems(),
            isLoading = state.upcoming is SectionUiState.Loading,
            errorMessage = (state.upcoming as? SectionUiState.Error)?.message,
            onRetry = { onEvent(HomeEvent.RetryUpcoming) },
            onMovieClick = {},
        )
    }
}

private fun SectionUiState.toSectionItems(): List<MovieSectionItem> =
    (this as? SectionUiState.Success)?.movies?.map { it.toSectionItem() } ?: emptyList()

private fun Movie.toSectionItem(): MovieSectionItem =
    MovieSectionItem(
        id = id,
        imageUrl = if (posterPath != null) "https://image.tmdb.org/t/p/w342$posterPath" else "",
        title = title,
    )
