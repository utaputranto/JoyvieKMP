package com.utaputranto.joyviekmp.feature.auth.presentation.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

/** Aggregates annotated auth-presentation definitions (@KoinViewModel) into a module hint. */
@Module
@ComponentScan("com.utaputranto.joyviekmp.feature.auth.presentation")
class AuthPresentationModule
