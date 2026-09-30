package com.utaputranto.joyviekmp.di

import com.utaputranto.joyviekmp.core.platform.DeviceInfo
import com.utaputranto.joyviekmp.core.platform.getDeviceInfo
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

/** Provides platform DeviceInfo from the expect/actual factory. */
@Module
class PlatformModule {
    @Single
    fun provideDeviceInfo(): DeviceInfo = getDeviceInfo()
}
