package com.utaputranto.joyviekmp.core.platform

enum class LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR,
}

object AppLogger {
    fun d(
        tag: String,
        message: String,
    ) = writeLog(LogLevel.DEBUG, tag, message, null)

    fun i(
        tag: String,
        message: String,
    ) = writeLog(LogLevel.INFO, tag, message, null)

    fun w(
        tag: String,
        message: String,
        throwable: Throwable? = null,
    ) = writeLog(LogLevel.WARN, tag, message, throwable)

    fun e(
        tag: String,
        message: String,
        throwable: Throwable? = null,
    ) = writeLog(LogLevel.ERROR, tag, message, throwable)
}

internal expect fun writeLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?,
)
