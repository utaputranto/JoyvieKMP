package com.utaputranto.joyviekmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform