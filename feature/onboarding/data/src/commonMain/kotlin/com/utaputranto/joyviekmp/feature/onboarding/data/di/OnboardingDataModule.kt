package com.utaputranto.joyviekmp.feature.onboarding.data.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

/** Aggregates annotated onboarding-data definitions (@Single repos/datasources) into a module hint. */
@Module
@ComponentScan("com.utaputranto.joyviekmp.feature.onboarding.data")
class OnboardingDataModule
