package com.utaputranto.joyviekmp.feature.onboarding.data.di

import com.utaputranto.joyviekmp.feature.onboarding.data.remote.TmdbApiService
import com.utaputranto.joyviekmp.feature.onboarding.data.remote.TmdbNetworkDataSource
import com.utaputranto.joyviekmp.feature.onboarding.data.repository.MovieRepositoryImpl
import com.utaputranto.joyviekmp.feature.onboarding.data.repository.OnboardingRepositoryImpl
import com.utaputranto.joyviekmp.feature.onboarding.domain.repository.MovieRepository
import com.utaputranto.joyviekmp.feature.onboarding.domain.repository.OnboardingRepository
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.GetPopularMoviesUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val onboardingDataModule: Module =
    module {
        single<OnboardingRepository> { OnboardingRepositoryImpl() }
        single<MovieRepository> { MovieRepositoryImpl(get()) }
        single { TmdbNetworkDataSource(get()) }
        single { TmdbApiService(get()) }
        factory { CompleteOnboardingUseCase(get()) }
        factory { GetPopularMoviesUseCase(get()) }
    }
