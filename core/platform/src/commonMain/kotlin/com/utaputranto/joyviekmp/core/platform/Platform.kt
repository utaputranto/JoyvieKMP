package com.utaputranto.joyviekmp.core.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
