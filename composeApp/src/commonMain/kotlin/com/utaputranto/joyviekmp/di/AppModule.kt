package com.utaputranto.joyviekmp.di

import com.utaputranto.joyviekmp.core.platform.getDeviceInfo
import com.utaputranto.joyviekmp.feature.auth.data.di.authDataModule
import com.utaputranto.joyviekmp.feature.auth.presentation.di.authPresentationModule
import com.utaputranto.joyviekmp.feature.onboarding.data.di.onboardingDataModule
import com.utaputranto.joyviekmp.feature.onboarding.presentation.di.onboardingPresentationModule
import org.koin.core.module.Module
import org.koin.dsl.module

val platformModule: Module =
    module {
        single { getDeviceInfo() }
    }

val appModules: List<Module> =
    listOf(
        platformModule,
        onboardingDataModule,
        onboardingPresentationModule,
        authDataModule,
        authPresentationModule,
    )
