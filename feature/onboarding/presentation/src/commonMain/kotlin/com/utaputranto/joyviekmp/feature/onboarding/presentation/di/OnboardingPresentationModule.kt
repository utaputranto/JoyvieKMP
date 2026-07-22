package com.utaputranto.joyviekmp.feature.onboarding.presentation.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

/** Aggregates annotated onboarding-presentation definitions (@KoinViewModel) into a module hint. */
@Module
@ComponentScan("com.utaputranto.joyviekmp.feature.onboarding.presentation")
class OnboardingPresentationModule
