package com.utaputranto.joyviekmp.core.network

open class ApiException(
    val statusCode: Int,
    override val message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

class UnauthorizedException(message: String, cause: Throwable? = null) : ApiException(401, message, cause)

class ForbiddenException(message: String, cause: Throwable? = null) : ApiException(403, message, cause)

class NotFoundException(message: String, cause: Throwable? = null) : ApiException(404, message, cause)

class ServerException(statusCode: Int, message: String, cause: Throwable? = null) : ApiException(statusCode, message, cause)
