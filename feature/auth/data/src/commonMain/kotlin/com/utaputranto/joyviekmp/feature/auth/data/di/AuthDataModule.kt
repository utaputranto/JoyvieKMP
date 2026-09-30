package com.utaputranto.joyviekmp.feature.auth.data.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

/** Aggregates annotated auth-data definitions (@Single repositories) into a module hint. */
@Module
@ComponentScan("com.utaputranto.joyviekmp.feature.auth.data")
class AuthDataModule
