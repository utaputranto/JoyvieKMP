package com.utaputranto.joyviekmp.feature.onboarding.data.repository

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.onboarding.data.mapper.toDomain
import com.utaputranto.joyviekmp.feature.onboarding.data.remote.TmdbNetworkDataSource
import com.utaputranto.joyviekmp.feature.onboarding.domain.repository.MovieRepository
import org.koin.core.annotation.Single

@Single(binds = [MovieRepository::class])
class MovieRepositoryImpl(
    private val apiService: TmdbNetworkDataSource,
) : MovieRepository {
    override suspend fun getPopularMovies(): Result<List<Movie>> {
        return apiService.getPopularMovies().map { response ->
            response.results.map { it.toDomain() }
        }
    }

    override suspend fun searchMovies(query: String): Result<List<Movie>> {
        return apiService.searchMovies(query).map { response ->
            response.results.map { it.toDomain() }
        }
    }
}
