package com.utaputranto.joyviekmp.core.network.di

import com.utaputranto.joyviekmp.core.network.TmdbNetworkDataSource
import com.utaputranto.joyviekmp.core.network.createHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule: Module =
    module {
        single { createHttpClient() }
        single { TmdbNetworkDataSource(get()) }
    }
