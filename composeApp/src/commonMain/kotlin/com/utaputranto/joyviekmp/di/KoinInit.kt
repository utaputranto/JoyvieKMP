package com.utaputranto.joyviekmp.di

import org.koin.mp.KoinPlatformTools
import org.koin.plugin.module.dsl.startKoin

/** Starts Koin once; safe to call from every platform entry point. */
fun initKoin() {
    if (KoinPlatformTools.defaultContext().getOrNull() == null) {
        // Typed startup from the Koin Compiler Plugin; modules loaded explicitly.
        startKoin<JoyvieKoinApp> {
            loadKoinModules()
        }
    }
}
