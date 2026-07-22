package com.utaputranto.joyviekmp.core.network.di

import com.utaputranto.joyviekmp.core.network.BuildConfig
import com.utaputranto.joyviekmp.core.network.TmdbHttpClientFactory
import com.utaputranto.joyviekmp.core.network.TmdbNetworkConfig
import io.ktor.client.HttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

/**
 * Provider functions for types built via factory calls / literal config, which can't be
 * constructor-injected. @ComponentScan also picks up this package's top-level @Single
 * (TmdbHttpClientFactory) and exposes the module hint for the app-level aggregator.
 */
@Module
@ComponentScan("com.utaputranto.joyviekmp.core.network")
class NetworkModule {
    @Single
    fun provideNetworkConfig(): TmdbNetworkConfig =
        TmdbNetworkConfig(
            baseUrl = "https://api.themoviedb.org/",
            tmdbToken = BuildConfig.TMDB_TOKEN,
        )

    @Single
    fun provideHttpClient(factory: TmdbHttpClientFactory): HttpClient = factory.build()
}
