package com.utaputranto.joyviekmp.feature.onboarding.domain.usecase

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.onboarding.domain.repository.MovieRepository
import org.koin.core.annotation.Factory

@Factory
class GetPopularMoviesUseCase(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(): Result<List<Movie>> = repository.getPopularMovies()
}
