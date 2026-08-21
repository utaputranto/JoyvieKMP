package com.utaputranto.joyviekmp.feature.home.data.repository

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.core.network.model.MovieResponseDto
import com.utaputranto.joyviekmp.core.network.safeApiCall
import com.utaputranto.joyviekmp.feature.home.data.mapper.toDomain
import com.utaputranto.joyviekmp.feature.home.domain.repository.HomeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Single

@Single(binds = [HomeRepository::class])
class HomeRepositoryImpl(
    private val httpClient: HttpClient,
) : HomeRepository {
    override suspend fun getNowPlayingMovies(): Result<List<Movie>> = fetchMovies(NOW_PLAYING_PATH)

    override suspend fun getTopRatedMovies(): Result<List<Movie>> = fetchMovies(TOP_RATED_PATH)

    override suspend fun getUpcomingMovies(): Result<List<Movie>> = fetchMovies(UPCOMING_PATH)

    private suspend fun fetchMovies(path: String): Result<List<Movie>> =
        safeApiCall {
            httpClient.get(path).body<MovieResponseDto>().results.map { it.toDomain() }
        }

    private companion object {
        const val NOW_PLAYING_PATH = "3/movie/now_playing"
        const val TOP_RATED_PATH = "3/movie/top_rated"
        const val UPCOMING_PATH = "3/movie/upcoming"
    }
}
