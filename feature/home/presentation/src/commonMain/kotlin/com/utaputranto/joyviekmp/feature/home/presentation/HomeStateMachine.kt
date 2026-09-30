package com.utaputranto.joyviekmp.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.utaputranto.joyviekmp.core.mvi.BaseStateMachine
import com.utaputranto.joyviekmp.feature.home.domain.usecase.GetNowPlayingMoviesUseCase
import com.utaputranto.joyviekmp.feature.home.domain.usecase.GetTopRatedMoviesUseCase
import com.utaputranto.joyviekmp.feature.home.domain.usecase.GetUpcomingMoviesUseCase
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeStateMachine(
    private val getNowPlayingMovies: GetNowPlayingMoviesUseCase,
    private val getTopRatedMovies: GetTopRatedMoviesUseCase,
    private val getUpcomingMovies: GetUpcomingMoviesUseCase,
) : BaseStateMachine<HomeState, HomeEvent, HomeEffect>(HomeState()) {
    init {
        onEvent(HomeEvent.LoadHomeMovies)
    }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.LoadHomeMovies -> loadAllSections()
            is HomeEvent.SelectTab -> setState { copy(selectedTab = event.tab) }
            HomeEvent.RetryNowPlaying -> loadNowPlaying()
            HomeEvent.RetryLatest -> loadLatest()
            HomeEvent.RetryUpcoming -> loadUpcoming()
        }
    }

    private fun loadAllSections() {
        loadNowPlaying()
        loadLatest()
        loadUpcoming()
    }

    private fun loadNowPlaying() {
        viewModelScope.launch {
            setState { copy(nowPlaying = SectionUiState.Loading) }
            getNowPlayingMovies().fold(
                onSuccess = { movies -> setState { copy(nowPlaying = SectionUiState.Success(movies)) } },
                onFailure = { error ->
                    setState { copy(nowPlaying = SectionUiState.Error(error.message ?: DEFAULT_ERROR_MESSAGE)) }
                },
            )
        }
    }

    private fun loadLatest() {
        viewModelScope.launch {
            setState { copy(latest = SectionUiState.Loading) }
            getTopRatedMovies().fold(
                onSuccess = { movies -> setState { copy(latest = SectionUiState.Success(movies)) } },
                onFailure = { error ->
                    setState { copy(latest = SectionUiState.Error(error.message ?: DEFAULT_ERROR_MESSAGE)) }
                },
            )
        }
    }

    private fun loadUpcoming() {
        viewModelScope.launch {
            setState { copy(upcoming = SectionUiState.Loading) }
            getUpcomingMovies().fold(
                onSuccess = { movies -> setState { copy(upcoming = SectionUiState.Success(movies)) } },
                onFailure = { error ->
                    setState { copy(upcoming = SectionUiState.Error(error.message ?: DEFAULT_ERROR_MESSAGE)) }
                },
            )
        }
    }

    private companion object {
        const val DEFAULT_ERROR_MESSAGE = "Something went wrong"
    }
}
