package com.utaputranto.joyviekmp.feature.home.data.repository

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.core.network.model.MovieDto
import com.utaputranto.joyviekmp.core.network.model.MovieResponseDto
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HomeRepositoryImplTest : FunSpec({

    val testJson = Json { ignoreUnknownKeys = true }

    val sampleDto =
        MovieDto(
            id = 42,
            title = "Sample Movie",
            overview = "A sample overview",
            posterPath = "/sample.jpg",
            releaseDate = "2026-03-01",
            voteAverage = 7.2,
        )
    val sampleMovie =
        Movie(
            id = 42,
            title = "Sample Movie",
            overview = "A sample overview",
            posterPath = "/sample.jpg",
            releaseDate = "2026-03-01",
            voteAverage = 7.2,
        )

    fun clientReturning(
        dto: MovieResponseDto,
        expectedPath: String,
    ): HttpClient {
        val engine =
            MockEngine { request ->
                request.url.encodedPath shouldBe "/$expectedPath"
                respond(
                    content = testJson.encodeToString(MovieResponseDto.serializer(), dto),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }
        return HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) { json(testJson) }
        }
    }

    fun clientReturningError(statusCode: HttpStatusCode): HttpClient {
        val engine = MockEngine { respondError(statusCode) }
        return HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) { json(testJson) }
        }
    }

    test("getNowPlayingMovies should map response to domain movies") {
        val responseDto = MovieResponseDto(page = 1, results = listOf(sampleDto), totalPages = 1, totalResults = 1)
        val repository = HomeRepositoryImpl(clientReturning(responseDto, "3/movie/now_playing"))

        val actual = repository.getNowPlayingMovies()

        actual shouldBe Result.success(listOf(sampleMovie))
    }

    test("getNowPlayingMovies should return failure on HTTP error") {
        val repository = HomeRepositoryImpl(clientReturningError(HttpStatusCode.InternalServerError))

        val actual = repository.getNowPlayingMovies()

        actual.isFailure shouldBe true
    }

    test("getTopRatedMovies should map response to domain movies") {
        val responseDto = MovieResponseDto(page = 1, results = listOf(sampleDto), totalPages = 1, totalResults = 1)
        val repository = HomeRepositoryImpl(clientReturning(responseDto, "3/movie/top_rated"))

        val actual = repository.getTopRatedMovies()

        actual shouldBe Result.success(listOf(sampleMovie))
    }

    test("getUpcomingMovies should map response to domain movies") {
        val responseDto = MovieResponseDto(page = 1, results = listOf(sampleDto), totalPages = 1, totalResults = 1)
        val repository = HomeRepositoryImpl(clientReturning(responseDto, "3/movie/upcoming"))

        val actual = repository.getUpcomingMovies()

        actual shouldBe Result.success(listOf(sampleMovie))
    }
})
