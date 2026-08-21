package com.utaputranto.joyviekmp.feature.home.domain.fakes

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.repository.HomeRepository

class FakeHomeRepository : HomeRepository {
    private var nowPlayingResult: Result<List<Movie>> = Result.success(emptyList())
    private var topRatedResult: Result<List<Movie>> = Result.success(emptyList())
    private var upcomingResult: Result<List<Movie>> = Result.success(emptyList())

    fun setNowPlayingResult(result: Result<List<Movie>>) {
        nowPlayingResult = result
    }

    fun setTopRatedResult(result: Result<List<Movie>>) {
        topRatedResult = result
    }

    fun setUpcomingResult(result: Result<List<Movie>>) {
        upcomingResult = result
    }

    override suspend fun getNowPlayingMovies(): Result<List<Movie>> = nowPlayingResult

    override suspend fun getTopRatedMovies(): Result<List<Movie>> = topRatedResult

    override suspend fun getUpcomingMovies(): Result<List<Movie>> = upcomingResult
}
