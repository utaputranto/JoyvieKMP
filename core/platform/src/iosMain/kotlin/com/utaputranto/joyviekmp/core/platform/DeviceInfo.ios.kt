package com.utaputranto.joyviekmp.core.platform

import platform.UIKit.UIDevice
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform as NativePlatform

@OptIn(ExperimentalNativeApi::class)
actual fun getDeviceInfo(): DeviceInfo =
    DeviceInfo(
        osName = UIDevice.currentDevice.systemName(),
        osVersion = UIDevice.currentDevice.systemVersion,
        isDebug = NativePlatform.isDebugBinary,
    )
