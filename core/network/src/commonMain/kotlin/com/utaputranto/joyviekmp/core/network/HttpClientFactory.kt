package com.utaputranto.joyviekmp.core.network

import com.utaputranto.joyviekmp.core.network.model.TmdbErrorDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

data class TmdbNetworkConfig(
    val baseUrl: String = "https://api.themoviedb.org/",
    val tmdbToken: String,
    val defaultLanguage: String = "en-US",
)

@Single
class TmdbHttpClientFactory(
    private val config: TmdbNetworkConfig,
    private val engine: HttpClientEngine? = null,
) {
    private val jsonConfiguration =
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            coerceInputValues = true
        }

    fun build(): HttpClient {
        val clientBlock: io.ktor.client.HttpClientConfig<*>.() -> Unit = {
            install(ContentNegotiation) {
                json(jsonConfiguration)
            }

            expectSuccess = true

            HttpResponseValidator {
                handleResponseExceptionWithRequest { cause, _ ->
                    if (cause is ResponseException) {
                        throw handleResponseException(cause)
                    }
                }
            }

            defaultRequest {
                url(config.baseUrl)
                url {
                    parameters.append("language", config.defaultLanguage)
                }
                header(HttpHeaders.Authorization, "Bearer ${config.tmdbToken}")
                contentType(ContentType.Application.Json)
            }
        }

        return if (engine != null) {
            HttpClient(engine, clientBlock)
        } else {
            HttpClient(clientBlock)
        }
    }

    private suspend fun handleResponseException(cause: ResponseException): Throwable {
        val response = cause.response
        val statusCode = response.status.value

        val errorDto =
            try {
                response.body<TmdbErrorDto>()
            } catch (_: Exception) {
                null
            }

        val serverMessage =
            errorDto?.statusMessage
                ?: "HTTP request failed with status code $statusCode"

        return when (statusCode) {
            HttpStatusCode.Unauthorized.value ->
                UnauthorizedException(
                    serverMessage,
                    cause,
                )

            HttpStatusCode.Forbidden.value ->
                ForbiddenException(
                    "Forbidden access: $serverMessage",
                    cause,
                )

            HttpStatusCode.NotFound.value ->
                NotFoundException(
                    "Endpoint not found: $serverMessage",
                    cause,
                )

            in HttpStatusCode.InternalServerError.value..HttpStatusCode.GatewayTimeout.value -> {
                ServerException(
                    statusCode,
                    "Server Error: $serverMessage",
                    cause,
                )
            }

            else -> ApiException(statusCode, serverMessage, cause)
        }
    }
}
