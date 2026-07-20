package com.utaputranto.joyviekmp.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utaputranto.joyviekmp.core.platform.AppLogger
import com.utaputranto.joyviekmp.core.platform.DeviceInfo
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val completeOnboarding: CompleteOnboardingUseCase,
    private val getPopularMovies: GetPopularMoviesUseCase,
    val deviceInfo: DeviceInfo,
) : ViewModel() {
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    fun finishOnboarding(onFinished: () -> Unit) {
        viewModelScope.launch {
            completeOnboarding()
            AppLogger.i(TAG, "Onboarding completed")
            _toastEvent.emit("Onboarding Completed!")
            onFinished()
        }
    }

    fun testHitEndpoint() {
        viewModelScope.launch {
            val result = getPopularMovies()
            result.onSuccess { movies ->
                AppLogger.i(TAG, "Successfully fetched popular movies: $movies")
                _toastEvent.emit("Success fetch ${movies.size} popular movies")
            }.onFailure { throwable ->
                AppLogger.i(TAG, "Failed to fetch popular movies: ${throwable.message}")
                _toastEvent.emit("Error: ${throwable.message ?: "Unknown error"}")
            }
        }
    }

    private companion object {
        const val TAG = "OnboardingViewModel"
    }
}
