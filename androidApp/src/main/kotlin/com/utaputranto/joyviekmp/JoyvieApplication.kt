package com.utaputranto.joyviekmp

import android.app.Application
import com.utaputranto.joyviekmp.di.JoyvieKoinApp
import com.utaputranto.joyviekmp.di.loadKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.plugin.module.dsl.startKoin

class JoyvieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin<JoyvieKoinApp> {
            androidContext(this@JoyvieApplication)
            loadKoinModules()
        }
    }
}
