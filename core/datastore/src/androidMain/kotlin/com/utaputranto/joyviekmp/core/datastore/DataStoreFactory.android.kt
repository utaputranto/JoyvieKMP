package com.utaputranto.joyviekmp.core.datastore

import android.content.Context
import org.koin.java.KoinJavaComponent.getKoin

actual fun dataStorePreferencesPath(): String {
    val context: Context = getKoin().get()
    return context.filesDir.resolve("joyvie.preferences_pb").absolutePath
}
