package com.utaputranto.joyviekmp.core.datastore

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlin.random.Random

class DataStoreFactoryTest : FunSpec({

    test("createDataStore should produce working DataStore instance with given producePath") {
        val testKey = stringPreferencesKey("factory_test_key")
        val expectedValue = "factory_value_${Random.nextInt()}"

        val dataStore =
            createDataStore {
                "build/test_factory_${Random.nextInt()}.preferences_pb"
            }

        dataStore.edit { preferences ->
            preferences[testKey] = expectedValue
        }

        val actualValue = dataStore.data.first()[testKey]
        actualValue shouldBe expectedValue
    }
})
