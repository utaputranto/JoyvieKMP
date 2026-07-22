package com.utaputranto.joyviekmp.core.platform

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import org.koin.java.KoinJavaComponent.getKoin

actual fun getDeviceInfo(): DeviceInfo {
    val context: Context = getKoin().get()
    val isDebuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    return DeviceInfo(
        osName = "Android",
        osVersion = "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
        isDebug = isDebuggable,
    )
}
