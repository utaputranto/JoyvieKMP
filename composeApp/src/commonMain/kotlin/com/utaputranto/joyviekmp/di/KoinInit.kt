package com.utaputranto.joyviekmp.di

import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatformTools

/** Starts Koin once; safe to call from every platform entry point. */
fun initKoin() {
    if (KoinPlatformTools.defaultContext().getOrNull() == null) {
        startKoin {
            modules(appModules)
        }
    }
}
