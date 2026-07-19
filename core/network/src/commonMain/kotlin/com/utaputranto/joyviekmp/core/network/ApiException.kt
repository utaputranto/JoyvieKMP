package com.utaputranto.joyviekmp.core.network

import io.ktor.http.HttpStatusCode

open class ApiException(
    val statusCode: Int,
    override val message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

class UnauthorizedException(message: String, cause: Throwable? = null) :
    ApiException(HttpStatusCode.Unauthorized.value, message, cause)

class ForbiddenException(message: String, cause: Throwable? = null) :
    ApiException(HttpStatusCode.Forbidden.value, message, cause)

class NotFoundException(message: String, cause: Throwable? = null) :
    ApiException(HttpStatusCode.NotFound.value, message, cause)

class ServerException(statusCode: Int, message: String, cause: Throwable? = null) :
    ApiException(statusCode, message, cause)
