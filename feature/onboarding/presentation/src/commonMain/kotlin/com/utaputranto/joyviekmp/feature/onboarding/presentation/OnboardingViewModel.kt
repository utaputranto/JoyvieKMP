package com.utaputranto.joyviekmp.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private companion object {
        const val TAG = "OnboardingViewModel"
    }
}
