package com.utaputranto.joyviekmp.feature.onboarding.data.remote

import com.utaputranto.joyviekmp.core.network.model.MovieResponseDto
import com.utaputranto.joyviekmp.core.network.safeApiCall
import org.koin.core.annotation.Single

@Single
class TmdbNetworkDataSource(
    private val apiService: TmdbApiService,
) {
    suspend fun getPopularMovies(): Result<MovieResponseDto> {
        return safeApiCall { apiService.getPopularMovies() }
    }

    suspend fun searchMovies(query: String): Result<MovieResponseDto> {
        return safeApiCall { apiService.searchMovies(query) }
    }
}
