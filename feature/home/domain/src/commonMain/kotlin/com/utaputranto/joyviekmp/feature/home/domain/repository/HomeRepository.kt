package com.utaputranto.joyviekmp.feature.home.domain.repository

import com.utaputranto.joyviekmp.core.model.Movie

interface HomeRepository {
    suspend fun getNowPlayingMovies(): Result<List<Movie>>

    suspend fun getTopRatedMovies(): Result<List<Movie>>

    suspend fun getUpcomingMovies(): Result<List<Movie>>
}
