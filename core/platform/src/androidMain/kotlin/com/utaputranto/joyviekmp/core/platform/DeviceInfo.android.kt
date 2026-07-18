package com.utaputranto.joyviekmp.core.platform

import android.content.pm.ApplicationInfo
import android.os.Build

actual fun getDeviceInfo(): DeviceInfo {
    val context = PlatformContextProvider.context
    val isDebuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    return DeviceInfo(
        osName = "Android",
        osVersion = "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
        isDebug = isDebuggable,
    )
}
