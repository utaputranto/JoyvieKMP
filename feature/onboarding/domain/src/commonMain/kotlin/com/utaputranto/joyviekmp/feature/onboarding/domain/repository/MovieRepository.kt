package com.utaputranto.joyviekmp.feature.onboarding.domain.repository

import com.utaputranto.joyviekmp.core.model.Movie

interface MovieRepository {
    suspend fun getPopularMovies(): Result<List<Movie>>

    suspend fun searchMovies(query: String): Result<List<Movie>>
}
