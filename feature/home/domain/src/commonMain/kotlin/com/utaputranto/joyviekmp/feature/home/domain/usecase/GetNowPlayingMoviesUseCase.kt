package com.utaputranto.joyviekmp.feature.home.domain.usecase

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.repository.HomeRepository
import org.koin.core.annotation.Factory

@Factory
class GetNowPlayingMoviesUseCase(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(): Result<List<Movie>> = repository.getNowPlayingMovies()
}
