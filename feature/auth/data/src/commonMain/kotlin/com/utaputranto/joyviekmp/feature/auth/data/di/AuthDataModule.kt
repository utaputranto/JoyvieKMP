package com.utaputranto.joyviekmp.feature.auth.data.di

import com.utaputranto.joyviekmp.feature.auth.data.repository.AuthRepositoryImpl
import com.utaputranto.joyviekmp.feature.auth.domain.repository.AuthRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val authDataModule: Module =
    module {
        single<AuthRepository> { AuthRepositoryImpl() }
    }
