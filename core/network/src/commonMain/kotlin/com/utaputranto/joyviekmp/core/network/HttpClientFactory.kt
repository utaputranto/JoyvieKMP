package com.utaputranto.joyviekmp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                },
            )
        }
        expectSuccess = true

        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, _ ->
                if (cause is io.ktor.client.plugins.ResponseException) {
                    val response = cause.response
                    val statusCode = response.status.value
                    val message = "HTTP request failed with status code $statusCode"

                    val apiException =
                        when (statusCode) {
                            401 -> UnauthorizedException("Unauthorized: Please check your TMDB token", cause)
                            403 -> ForbiddenException("Forbidden access: $message", cause)
                            404 -> NotFoundException("Endpoint not found: $message", cause)
                            in 500..599 -> ServerException(statusCode, "Server Error: $message", cause)
                            else -> ApiException(statusCode, message, cause)
                        }
                    throw apiException
                }
            }
        }

        defaultRequest {
            url("https://api.themoviedb.org/")
            url {
                parameters.append("language", "en-US")
            }
            header(HttpHeaders.Authorization, "Bearer ${BuildConfig.TMDB_TOKEN}")
        }
    }
}
