package com.utaputranto.joyviekmp.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.utaputranto.joyviekmp.core.datastore.createDataStore
import com.utaputranto.joyviekmp.core.datastore.dataStorePreferencesPath
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

/** Provides the DataStore; @ComponentScan also grabs DataStoreManagerImpl (@Single). */
@Module
@ComponentScan("com.utaputranto.joyviekmp.core.datastore")
class DataStoreModule {
    @Single
    fun provideDataStore(): DataStore<Preferences> = createDataStore { dataStorePreferencesPath() }
}
