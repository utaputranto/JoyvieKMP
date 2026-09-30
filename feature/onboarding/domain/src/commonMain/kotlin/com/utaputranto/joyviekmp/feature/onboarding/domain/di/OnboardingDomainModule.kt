package com.utaputranto.joyviekmp.feature.onboarding.domain.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

/** Aggregates annotated onboarding-domain definitions (@Factory use cases) into a module hint. */
@Module
@ComponentScan("com.utaputranto.joyviekmp.feature.onboarding.domain")
class OnboardingDomainModule
