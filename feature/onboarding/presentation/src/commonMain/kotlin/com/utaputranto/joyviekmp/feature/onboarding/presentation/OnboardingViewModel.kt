package com.utaputranto.joyviekmp.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utaputranto.joyviekmp.core.platform.AppLogger
import com.utaputranto.joyviekmp.core.platform.DeviceInfo
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val completeOnboarding: CompleteOnboardingUseCase,
    private val getPopularMovies: GetPopularMoviesUseCase,
    val deviceInfo: DeviceInfo,
) : ViewModel() {
    fun finishOnboarding(onFinished: () -> Unit) {
        viewModelScope.launch {
            completeOnboarding()
            AppLogger.i(TAG, "Onboarding completed")
            onFinished()
        }
    }

    fun testHitEndpoint() {
        viewModelScope.launch {
            val result = getPopularMovies()
            result.onSuccess {
                AppLogger.i(TAG, "Successfully fetched popular movies: $it")
            }.onFailure {
                AppLogger.i(TAG, "Failed to fetch popular movies: ${it.message}")
            }
        }
    }

    private companion object {
        const val TAG = "OnboardingViewModel"
    }
}
