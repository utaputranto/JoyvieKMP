package com.utaputranto.joyviekmp.feature.auth.domain.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

/** Aggregates annotated auth-domain definitions (@Factory use cases) into a module hint. */
@Module
@ComponentScan("com.utaputranto.joyviekmp.feature.auth.domain")
class AuthDomainModule
