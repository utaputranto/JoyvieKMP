package com.utaputranto.joyviekmp.feature.home.domain.usecase

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.feature.home.domain.fakes.FakeHomeRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GetTopRatedMoviesUseCaseTest : FunSpec({

    lateinit var fakeRepository: FakeHomeRepository
    lateinit var useCase: GetTopRatedMoviesUseCase

    beforeTest {
        fakeRepository = FakeHomeRepository()
        useCase = GetTopRatedMoviesUseCase(fakeRepository)
    }

    test("invoke should return top rated movies from repository") {
        val expectedMovies =
            listOf(
                Movie(
                    id = 2,
                    title = "Top Rated Movie",
                    overview = "Overview",
                    posterPath = "/poster2.jpg",
                    releaseDate = "2025-06-01",
                    voteAverage = 8.9,
                ),
            )
        fakeRepository.setTopRatedResult(Result.success(expectedMovies))

        val actual = useCase()

        actual shouldBe Result.success(expectedMovies)
    }

    test("invoke should propagate failure from repository") {
        val expectedError = RuntimeException("network error")
        fakeRepository.setTopRatedResult(Result.failure(expectedError))

        val actual = useCase()

        actual shouldBe Result.failure<List<Movie>>(expectedError)
    }
})
