package com.utaputranto.joyviekmp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath
import kotlin.random.Random

@Serializable
data class TestUser(
    val id: Int,
    val name: String,
    val email: String,
)

class DataStoreExtensionsTest : FunSpec({

    val userKey = stringPreferencesKey("test_user")

    lateinit var dataStore: DataStore<Preferences>
    lateinit var dataStoreManager: DataStoreManager

    beforeTest {
        val testPath = "build/test_datastore_${Random.nextInt()}.preferences_pb".toPath()
        dataStore =
            PreferenceDataStoreFactory.createWithPath(
                produceFile = { testPath },
            )
        dataStoreManager = DataStoreManagerImpl(dataStore)
    }

    test("putObject and getObject should save and retrieve serializable data class object") {
        val expectedUser =
            TestUser(
                id = 101,
                name = "John Doe",
                email = "john.doe@example.com",
            )

        dataStore.putObject(userKey, expectedUser)
        val actualUser = dataStore.getObject<TestUser>(userKey).first()

        actualUser shouldBe expectedUser
    }

    test("getObject should return null when key does not exist") {
        val actualUser = dataStore.getObject<TestUser>(userKey).first()

        actualUser shouldBe null
    }

    test("getObject should return null when json string is corrupted") {
        dataStore.edit { preferences ->
            preferences[userKey] = "invalid_json_{corrupted}"
        }

        val actualUser = dataStore.getObject<TestUser>(userKey).first()

        actualUser shouldBe null
    }

    test("Preferences.getObject should read object directly from Preferences snapshot with default json") {
        val expectedUser =
            TestUser(
                id = 103,
                name = "Alice",
                email = "alice@example.com",
            )
        dataStore.putObject(userKey, expectedUser)

        val preferencesSnapshot = dataStore.data.first()
        val actualUser = preferencesSnapshot.getObject<TestUser>(userKey)

        actualUser shouldBe expectedUser
    }

    test("Preferences.getObject should return null when key does not exist on Preferences snapshot") {
        val preferencesSnapshot = dataStore.data.first()
        val actualUser = preferencesSnapshot.getObject<TestUser>(userKey)

        actualUser shouldBe null
    }

    test("Preferences.getObject should return null when value is corrupted on Preferences snapshot") {
        dataStore.edit { preferences ->
            preferences[userKey] = "{bad_json:true"
        }

        val preferencesSnapshot = dataStore.data.first()
        val actualUser = preferencesSnapshot.getObject<TestUser>(userKey)

        actualUser shouldBe null
    }

    test("Preferences.getObject with custom Json instance should decode successfully") {
        val customJson = Json { ignoreUnknownKeys = true }
        val expectedUser =
            TestUser(
                id = 105,
                name = "Charlie",
                email = "charlie@example.com",
            )
        dataStore.putObject(userKey, expectedUser, json = customJson)

        val preferencesSnapshot = dataStore.data.first()
        val actualUser = preferencesSnapshot.getObject<TestUser>(userKey, json = customJson)

        actualUser shouldBe expectedUser
    }

    test("putObject and getObject with custom Json instance should work properly") {
        val customJson = Json { prettyPrint = true }
        val expectedUser =
            TestUser(
                id = 104,
                name = "Bob",
                email = "bob@example.com",
            )

        dataStore.putObject(userKey, expectedUser, json = customJson)
        val actualUser = dataStore.getObject<TestUser>(userKey, json = customJson).first()

        actualUser shouldBe expectedUser
    }

    test("removeObject should delete serializable data class object from dataStore") {
        val initialUser =
            TestUser(
                id = 102,
                name = "Jane Doe",
                email = "jane.doe@example.com",
            )
        dataStore.putObject(userKey, initialUser)

        dataStore.removeObject(userKey)
        val actualUser = dataStore.getObject<TestUser>(userKey).first()

        actualUser shouldBe null
    }

    test("DataStoreManager.getObject should return null when key does not exist") {
        val actualUser = dataStoreManager.getObject<TestUser>(userKey).first()

        actualUser shouldBe null
    }

    test("DataStoreManager.getObject should return null when json string is corrupted") {
        dataStoreManager.put(userKey, "{invalid_json:corrupted")

        val actualUser = dataStoreManager.getObject<TestUser>(userKey).first()

        actualUser shouldBe null
    }

    test("DataStoreManager.putObject and getObject with custom Json should work properly") {
        val customJson = Json { prettyPrint = true }
        val expectedUser =
            TestUser(
                id = 106,
                name = "David",
                email = "david@example.com",
            )

        dataStoreManager.putObject(userKey, expectedUser, json = customJson)
        val actualUser = dataStoreManager.getObject<TestUser>(userKey, json = customJson).first()

        actualUser shouldBe expectedUser
    }
})
