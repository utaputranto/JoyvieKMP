package com.utaputranto.joyviekmp.core.platform

import platform.Foundation.NSLog

internal actual fun writeLog(
    level: LogLevel,
    tag: String,
    message: String,
    throwable: Throwable?,
) {
    val suffix = throwable?.let { " | ${it.stackTraceToString()}" }.orEmpty()
    val line = "[${level.name}] $tag: $message$suffix"
    // NSLog treats its first argument as a format string; escape '%' and avoid
    // varargs since Kotlin String -> ObjC id bridging is unsafe in vararg position.
    NSLog(line.replace("%", "%%"))
}
