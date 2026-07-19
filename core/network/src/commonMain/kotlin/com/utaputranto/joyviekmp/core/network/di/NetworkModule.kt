package com.utaputranto.joyviekmp.core.network.di

import com.utaputranto.joyviekmp.core.network.BuildConfig
import com.utaputranto.joyviekmp.core.network.TmdbHttpClientFactory
import com.utaputranto.joyviekmp.core.network.TmdbNetworkConfig
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule: Module =
    module {
        single {
            TmdbNetworkConfig(
                baseUrl = "https://api.themoviedb.org/",
                tmdbToken = BuildConfig.TMDB_TOKEN,
            )
        }

        single {
            TmdbHttpClientFactory(
                config = get(),
            )
        }

        single {
            get<TmdbHttpClientFactory>().build()
        }
    }
