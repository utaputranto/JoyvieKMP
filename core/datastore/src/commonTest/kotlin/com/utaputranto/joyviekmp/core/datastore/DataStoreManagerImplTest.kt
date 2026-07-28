package com.utaputranto.joyviekmp.core.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
private data class SampleObject(val id: Int, val name: String)

class DataStoreManagerImplTest : BehaviorSpec({

    lateinit var dataStoreManager: DataStoreManagerImpl

    beforeTest {
        val dataStore =
            createDataStore {
                "build/test_datastore_manager_${Random.nextInt()}.preferences_pb"
            }
        dataStoreManager = DataStoreManagerImpl(dataStore)
    }

    Given("DataStoreManagerImpl") {
        val stringKey = stringPreferencesKey("test_string_key")
        val intKey = intPreferencesKey("test_int_key")
        val boolKey = booleanPreferencesKey("test_bool_key")

        When("reading non-existent key with getOrDefault") {
            Then("it should return default value") {
                val value = dataStoreManager.get(stringKey, "default_str").first()
                value shouldBe "default_str"
            }
        }

        When("reading non-existent key with getOrNull") {
            Then("it should return null") {
                val value = dataStoreManager.getOrNull(stringKey).first()
                value shouldBe null
            }
        }

        When("putting value and reading it") {
            Then("it should return saved value") {
                dataStoreManager.put(stringKey, "hello_world")
                dataStoreManager.put(intKey, 42)
                dataStoreManager.put(boolKey, true)

                dataStoreManager.get(stringKey, "").first() shouldBe "hello_world"
                dataStoreManager.get(intKey, 0).first() shouldBe 42
                dataStoreManager.get(boolKey, false).first() shouldBe true
            }
        }

        When("removing a saved key") {
            Then("it should revert to default value and getOrNull to null") {
                dataStoreManager.put(stringKey, "to_be_removed")
                dataStoreManager.get(stringKey, "").first() shouldBe "to_be_removed"

                dataStoreManager.remove(stringKey)

                dataStoreManager.get(stringKey, "default_after_remove").first() shouldBe "default_after_remove"
                dataStoreManager.getOrNull(stringKey).first() shouldBe null
            }
        }

        When("clearing data store") {
            Then("all keys should be cleared") {
                dataStoreManager.put(stringKey, "val1")
                dataStoreManager.put(intKey, 100)

                dataStoreManager.clear()

                dataStoreManager.getOrNull(stringKey).first() shouldBe null
                dataStoreManager.getOrNull(intKey).first() shouldBe null
            }
        }

        When("saving and reading object using getObject/putObject extensions") {
            Then("it should serialize and deserialize correctly") {
                val objectKey = stringPreferencesKey("sample_object_key")
                val sampleObj = SampleObject(id = 1, name = "Joyvie")

                dataStoreManager.putObject(objectKey, sampleObj)

                val retrievedObj = dataStoreManager.getObject<SampleObject>(objectKey).first()
                retrievedObj shouldBe sampleObj
            }
        }
    }
})
