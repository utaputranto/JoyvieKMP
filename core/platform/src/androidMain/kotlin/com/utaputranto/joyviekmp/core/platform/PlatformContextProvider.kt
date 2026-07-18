package com.utaputranto.joyviekmp.core.platform

import android.content.Context

object PlatformContextProvider {
    @Volatile
    private var appContext: Context? = null

    val context: Context
        get() =
            checkNotNull(appContext) {
                "PlatformContextProvider has not been initialized. " +
                    "Call PlatformContextProvider.init(context) from Application.onCreate()."
            }

    fun init(context: Context) {
        appContext = context.applicationContext
    }
}
