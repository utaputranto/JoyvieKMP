package com.utaputranto.joyviekmp.core.platform

data class DeviceInfo(
    val osName: String,
    val osVersion: String,
    val isDebug: Boolean,
) {
    val isRelease: Boolean get() = !isDebug
}

expect fun getDeviceInfo(): DeviceInfo
