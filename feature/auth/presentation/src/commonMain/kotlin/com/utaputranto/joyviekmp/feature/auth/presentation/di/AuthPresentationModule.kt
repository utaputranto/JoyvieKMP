package com.utaputranto.joyviekmp.feature.auth.presentation.di

import com.utaputranto.joyviekmp.feature.auth.domain.usecase.LoginUseCase
import com.utaputranto.joyviekmp.feature.auth.presentation.AuthViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authPresentationModule: Module =
    module {
        factory { LoginUseCase(get()) }
        viewModel { AuthViewModel(get()) }
    }
