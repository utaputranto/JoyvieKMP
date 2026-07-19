package com.utaputranto.joyviekmp.core.network

import com.utaputranto.joyviekmp.core.network.model.MovieResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlin.coroutines.cancellation.CancellationException

class TmdbNetworkDataSource(
    private val httpClient: HttpClient,
) {
    suspend fun getPopularMovies(): Result<MovieResponseDto> {
        return try {
            val response = httpClient.get("3/movie/popular")
            Result.success(response.body())
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    suspend fun searchMovies(query: String): Result<MovieResponseDto> {
        return try {
            val response =
                httpClient.get("3/search/movie") {
                    parameter("query", query)
                }
            Result.success(response.body())
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }
}
