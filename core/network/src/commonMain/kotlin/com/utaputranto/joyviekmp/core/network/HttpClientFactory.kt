package com.utaputranto.joyviekmp.core.network

import io.ktor.client.HttpClient
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
        defaultRequest {
            url("https://api.themoviedb.org/")
            url {
                parameters.append("language", "en-US")
            }
            header(HttpHeaders.Authorization, "Bearer TODO(\"Provide TMDB Token\")")
        }
    }
}
