package com.utaputranto.joyviekmp.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utaputranto.joyviekmp.core.platform.AppLogger
import com.utaputranto.joyviekmp.feature.onboarding.domain.model.OnboardingPage
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.GetIsCompletedOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.GetOnboardingPagesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val completeOnboarding: CompleteOnboardingUseCase,
    private val getOnboardingPages: GetOnboardingPagesUseCase,
    private val getIsCompletedOnboardingUseCase: GetIsCompletedOnboardingUseCase,
) : ViewModel() {
    private val _toastEvent = Channel<String>(Channel.BUFFERED)
    val toastEvent: Flow<String> = _toastEvent.receiveAsFlow()

    private val _pages = MutableStateFlow<List<OnboardingPage>>(emptyList())
    val pages: StateFlow<List<OnboardingPage>> = _pages.asStateFlow()

    init {
        loadPages()
        observeCache()
    }

    private fun loadPages() {
        viewModelScope.launch {
            _pages.value = getOnboardingPages()
        }
    }

    fun observeCache() {
        viewModelScope.launch {
            if (getIsCompletedOnboardingUseCase.invoke()) {
                _toastEvent.send("Onboarding Completed!")
            } else {
                _toastEvent.send("Onboarding UnCompleted!")
            }
        }
    }

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
