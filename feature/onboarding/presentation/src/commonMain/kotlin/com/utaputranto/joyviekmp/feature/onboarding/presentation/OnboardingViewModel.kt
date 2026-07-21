package com.utaputranto.joyviekmp.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utaputranto.joyviekmp.core.platform.AppLogger
import com.utaputranto.joyviekmp.core.platform.DeviceInfo
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.CheckOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val checkOnboarding: CheckOnboardingUseCase,
    private val completeOnboarding: CompleteOnboardingUseCase,
    private val getPopularMovies: GetPopularMoviesUseCase,
    val deviceInfo: DeviceInfo,
) : ViewModel() {
    private val _isOnboardingCompleted = MutableStateFlow<Boolean?>(null)
    val isOnboardingCompleted: StateFlow<Boolean?> = _isOnboardingCompleted.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    fun checkOnboardingStatus() {
        viewModelScope.launch {
            // Introduce a brief delay for the splash screen
            delay(2000L)
            _isOnboardingCompleted.value = checkOnboarding()
        }
    }

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
