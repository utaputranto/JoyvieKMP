package com.utaputranto.joyviekmp.feature.home.presentation

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.core.mvi.UiEffect
import com.utaputranto.joyviekmp.core.mvi.UiEvent
import com.utaputranto.joyviekmp.core.mvi.UiState

/** Bottom navigation destinations for the home feature. */
enum class HomeTab { Home, Search, Profile }

/** Independent loading/success/error state for a single movie section. */
sealed interface SectionUiState {
    data object Loading : SectionUiState

    data class Success(val movies: List<Movie>) : SectionUiState

    data class Error(val message: String) : SectionUiState
}

/**
 * Immutable UI state for the home feature.
 *
 * @param selectedTab Currently active bottom-nav tab.
 * @param nowPlaying State of the "Now Playing" movie section.
 * @param latest State of the "Latest" (top rated) movie section.
 * @param upcoming State of the "Upcoming" movie section.
 */
data class HomeState(
    val selectedTab: HomeTab = HomeTab.Home,
    val nowPlaying: SectionUiState = SectionUiState.Loading,
    val latest: SectionUiState = SectionUiState.Loading,
    val upcoming: SectionUiState = SectionUiState.Loading,
) : UiState

/** User intents and UI events for the home feature. */
sealed interface HomeEvent : UiEvent {
    data object LoadHomeMovies : HomeEvent

    data class SelectTab(val tab: HomeTab) : HomeEvent

    data object RetryNowPlaying : HomeEvent

    data object RetryLatest : HomeEvent

    data object RetryUpcoming : HomeEvent
}

/** One-shot side effects for the home feature (none needed yet). */
sealed interface HomeEffect : UiEffect
