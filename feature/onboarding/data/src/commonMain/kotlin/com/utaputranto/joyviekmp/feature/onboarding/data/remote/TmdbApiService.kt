package com.utaputranto.joyviekmp.feature.onboarding.data.remote

import com.utaputranto.joyviekmp.core.network.TmdbHttpClientFactory
import com.utaputranto.joyviekmp.core.network.model.MovieResponseDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TmdbApiService(private val client: TmdbHttpClientFactory) {
    suspend fun getPopularMovies(): MovieResponseDto {
        return client.build().get("3/movie/popularr").body()
    }

    suspend fun searchMovies(query: String): MovieResponseDto {
        return client.build().get("3/search/movie") {
            parameter("query", query)
        }.body()
    }
}
