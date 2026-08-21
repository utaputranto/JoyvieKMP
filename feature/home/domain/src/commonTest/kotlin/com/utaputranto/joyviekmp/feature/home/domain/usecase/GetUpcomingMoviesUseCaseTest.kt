package com.utaputranto.joyviekmp.feature.home.domain.usecase

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.fakes.FakeHomeRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GetUpcomingMoviesUseCaseTest : FunSpec({

    lateinit var fakeRepository: FakeHomeRepository
    lateinit var useCase: GetUpcomingMoviesUseCase

    beforeTest {
        fakeRepository = FakeHomeRepository()
        useCase = GetUpcomingMoviesUseCase(fakeRepository)
    }

    test("invoke should return upcoming movies from repository") {
        val expectedMovies =
            listOf(
                Movie(
                    id = 3,
                    title = "Upcoming Movie",
                    overview = "Overview",
                    posterPath = "/poster3.jpg",
                    releaseDate = "2026-12-01",
                    voteAverage = 0.0,
                ),
            )
        fakeRepository.setUpcomingResult(Result.success(expectedMovies))

        val actual = useCase()

        actual shouldBe Result.success(expectedMovies)
    }

    test("invoke should propagate failure from repository") {
        val expectedError = RuntimeException("network error")
        fakeRepository.setUpcomingResult(Result.failure(expectedError))

        val actual = useCase()

        actual shouldBe Result.failure<List<Movie>>(expectedError)
    }
})
