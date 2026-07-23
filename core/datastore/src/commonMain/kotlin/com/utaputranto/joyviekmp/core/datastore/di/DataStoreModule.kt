package com.utaputranto.joyviekmp.core.datastore.di

import com.utaputranto.joyviekmp.core.datastore.DataStoreManagerImpl
import com.utaputranto.joyviekmp.core.datastore.PreferenceStorage
import com.utaputranto.joyviekmp.core.datastore.createDataStore
import com.utaputranto.joyviekmp.core.datastore.dataStorePreferencesPath
import org.koin.core.module.Module
import org.koin.dsl.module

val dataStoreModule: Module =
    module {
        single { createDataStore { dataStorePreferencesPath() } }
        single<PreferenceStorage> { DataStoreManagerImpl(get()) }
    }
