package com.utaputranto.joyviekmp

import android.app.Application
import com.utaputranto.joyviekmp.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class JoyvieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@JoyvieApplication)
            modules(appModules)
        }
    }
}
