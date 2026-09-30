package com.utaputranto.joyviekmp.feature.home.presentation.fakes

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.repository.HomeRepository

class FakeHomeRepository : HomeRepository {
    private var nowPlayingResult: Result<List<Movie>> = Result.success(emptyList())
    private var topRatedResult: Result<List<Movie>> = Result.success(emptyList())
    private var upcomingResult: Result<List<Movie>> = Result.success(emptyList())

    var nowPlayingCallCount: Int = 0
        private set
    var topRatedCallCount: Int = 0
        private set
    var upcomingCallCount: Int = 0
        private set

    fun setNowPlayingResult(result: Result<List<Movie>>) {
        nowPlayingResult = result
    }

    fun setTopRatedResult(result: Result<List<Movie>>) {
        topRatedResult = result
    }

    fun setUpcomingResult(result: Result<List<Movie>>) {
        upcomingResult = result
    }

    override suspend fun getNowPlayingMovies(): Result<List<Movie>> {
        nowPlayingCallCount++
        return nowPlayingResult
    }

    override suspend fun getTopRatedMovies(): Result<List<Movie>> {
        topRatedCallCount++
        return topRatedResult
    }

    override suspend fun getUpcomingMovies(): Result<List<Movie>> {
        upcomingCallCount++
        return upcomingResult
    }
}
