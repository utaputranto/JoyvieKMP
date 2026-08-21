package com.utaputranto.joyviekmp.feature.home.presentation.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

/** Aggregates annotated home-presentation definitions (@KoinViewModel) into a module hint. */
@Module
@ComponentScan("com.utaputranto.joyviekmp.feature.home.presentation")
class HomePresentationModule
