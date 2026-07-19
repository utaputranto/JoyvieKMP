package com.utaputranto.joyviekmp.core.network

import kotlinx.coroutines.CancellationException

suspend inline fun <T> safeApiCall(crossinline block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: ApiException) {
        Result.failure(e)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(ApiException(statusCode = 0, message = e.message ?: "Unknown network error", cause = e))
    }
}
