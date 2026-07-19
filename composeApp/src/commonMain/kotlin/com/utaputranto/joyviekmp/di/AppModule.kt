package com.utaputranto.joyviekmp.di

import com.utaputranto.joyviekmp.core.platform.getDeviceInfo
import org.koin.core.module.Module
import org.koin.dsl.module

val platformModule: Module =
    module {
        single { getDeviceInfo() }
    }

val appModules: List<Module> = listOf(platformModule) + generatedModules
