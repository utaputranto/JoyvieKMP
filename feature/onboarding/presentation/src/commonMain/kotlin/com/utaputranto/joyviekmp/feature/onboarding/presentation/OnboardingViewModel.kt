package com.utaputranto.joyviekmp.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utaputranto.joyviekmp.core.network.TmdbNetworkDataSource
import com.utaputranto.joyviekmp.core.network.createHttpClient
import com.utaputranto.joyviekmp.core.platform.AppLogger
import com.utaputranto.joyviekmp.core.platform.DeviceInfo
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val completeOnboarding: CompleteOnboardingUseCase,
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
            val httpClient = createHttpClient()
            val tmdbNetworkDataSource = TmdbNetworkDataSource(httpClient)
            val result = tmdbNetworkDataSource.getPopularMovies()
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
