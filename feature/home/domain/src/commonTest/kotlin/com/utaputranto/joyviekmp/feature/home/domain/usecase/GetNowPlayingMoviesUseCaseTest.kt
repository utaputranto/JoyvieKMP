package com.utaputranto.joyviekmp.feature.home.domain.usecase

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.fakes.FakeHomeRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GetNowPlayingMoviesUseCaseTest : FunSpec({

    lateinit var fakeRepository: FakeHomeRepository
    lateinit var useCase: GetNowPlayingMoviesUseCase

    beforeTest {
        fakeRepository = FakeHomeRepository()
        useCase = GetNowPlayingMoviesUseCase(fakeRepository)
    }

    test("invoke should return now playing movies from repository") {
        val expectedMovies =
            listOf(
                Movie(
                    id = 1,
                    title = "Now Playing Movie",
                    overview = "Overview",
                    posterPath = "/poster.jpg",
                    releaseDate = "2026-01-01",
                    voteAverage = 7.5,
                ),
            )
        fakeRepository.setNowPlayingResult(Result.success(expectedMovies))

        val actual = useCase()

        actual shouldBe Result.success(expectedMovies)
    }

    test("invoke should propagate failure from repository") {
        val expectedError = RuntimeException("network error")
        fakeRepository.setNowPlayingResult(Result.failure(expectedError))

        val actual = useCase()

        actual shouldBe Result.failure<List<Movie>>(expectedError)
    }
})
