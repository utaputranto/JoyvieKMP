package com.utaputranto.joyviekmp

import android.app.Application
import com.utaputranto.joyviekmp.core.platform.PlatformContextProvider
import com.utaputranto.joyviekmp.di.appModules
import org.koin.core.context.startKoin

class JoyvieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PlatformContextProvider.init(this)
        startKoin {
            modules(appModules)
        }
    }
}
