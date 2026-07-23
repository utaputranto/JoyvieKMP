package com.utaputranto.joyviekmp.feature.onboarding.presentation.di

import com.utaputranto.joyviekmp.feature.onboarding.presentation.OnboardingViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onboardingPresentationModule: Module =
    module {
        viewModel { OnboardingViewModel(get(), get(), get()) }
    }
