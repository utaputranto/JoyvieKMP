package com.utaputranto.joyviekmp.di

import com.utaputranto.joyviekmp.core.datastore.di.DataStoreModule
import com.utaputranto.joyviekmp.core.network.di.NetworkModule
import com.utaputranto.joyviekmp.feature.auth.data.di.AuthDataModule
import com.utaputranto.joyviekmp.feature.auth.domain.di.AuthDomainModule
import com.utaputranto.joyviekmp.feature.auth.presentation.di.AuthPresentationModule
import com.utaputranto.joyviekmp.feature.onboarding.data.di.OnboardingDataModule
import com.utaputranto.joyviekmp.feature.onboarding.domain.di.OnboardingDomainModule
import com.utaputranto.joyviekmp.feature.onboarding.presentation.di.OnboardingPresentationModule
import org.koin.core.KoinApplication
import org.koin.plugin.module.dsl.modules
import org.koin.core.annotation.KoinApplication as KoinApplicationAnnotation

/**
 * Koin Compiler Plugin application entry. Every feature/core @Module is loaded explicitly
 * (deterministic) rather than relying on cross-module @ComponentScan discovery, which is
 * not reliable at runtime across module boundaries. Each @Module's own @ComponentScan
 * still registers its package's annotated definitions.
 */
@KoinApplicationAnnotation
class JoyvieKoinApp

/** Loads all annotated modules into the running KoinApplication. */
fun KoinApplication.loadKoinModules() {
    modules(
        PlatformModule::class,
        NetworkModule::class,
        DataStoreModule::class,
        AuthDomainModule::class,
        AuthDataModule::class,
        AuthPresentationModule::class,
        OnboardingDomainModule::class,
        OnboardingDataModule::class,
        OnboardingPresentationModule::class,
    )
}
