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

    beforeTest {
        val testPath = "build/test_datastore_${Random.nextInt()}.preferences_pb".toPath()
        dataStore =
            PreferenceDataStoreFactory.createWithPath(
                produceFile = { testPath },
            )
    }

    test("putObject and getObject should save and retrieve serializable data class object") {
        // Arrange
        val expectedUser =
            TestUser(
                id = 101,
                name = "John Doe",
                email = "john.doe@example.com",
            )

        // Act
        dataStore.putObject(userKey, expectedUser)
        val actualUser = dataStore.getObject<TestUser>(userKey).first()

        // Assert
        actualUser shouldBe expectedUser
    }

    test("getObject should return null when key does not exist") {
        // Arrange & Act
        val actualUser = dataStore.getObject<TestUser>(userKey).first()

        // Assert
        actualUser shouldBe null
    }

    test("getObject should return null when json string is corrupted") {
        // Arrange
        dataStore.edit { preferences ->
            preferences[userKey] = "invalid_json_{corrupted}"
        }

        // Act
        val actualUser = dataStore.getObject<TestUser>(userKey).first()

        // Assert
        actualUser shouldBe null
    }

    test("Preferences.getObject should read object directly from Preferences snapshot with default json") {
        // Arrange
        val expectedUser =
            TestUser(
                id = 103,
                name = "Alice",
                email = "alice@example.com",
            )
        dataStore.putObject(userKey, expectedUser)

        // Act
        val preferencesSnapshot = dataStore.data.first()
        val actualUser = preferencesSnapshot.getObject<TestUser>(userKey)

        // Assert
        actualUser shouldBe expectedUser
    }

    test("Preferences.getObject should return null when key does not exist on Preferences snapshot") {
        // Arrange & Act
        val preferencesSnapshot = dataStore.data.first()
        val actualUser = preferencesSnapshot.getObject<TestUser>(userKey)

        // Assert
        actualUser shouldBe null
    }

    test("Preferences.getObject should return null when value is corrupted on Preferences snapshot") {
        // Arrange
        dataStore.edit { preferences ->
            preferences[userKey] = "{bad_json:true"
        }

        // Act
        val preferencesSnapshot = dataStore.data.first()
        val actualUser = preferencesSnapshot.getObject<TestUser>(userKey)

        // Assert
        actualUser shouldBe null
    }

    test("Preferences.getObject with custom Json instance should decode successfully") {
        // Arrange
        val customJson = Json { ignoreUnknownKeys = true }
        val expectedUser =
            TestUser(
                id = 105,
                name = "Charlie",
                email = "charlie@example.com",
            )
        dataStore.putObject(userKey, expectedUser, json = customJson)

        // Act
        val preferencesSnapshot = dataStore.data.first()
        val actualUser = preferencesSnapshot.getObject<TestUser>(userKey, json = customJson)

        // Assert
        actualUser shouldBe expectedUser
    }

    test("putObject and getObject with custom Json instance should work properly") {
        // Arrange
        val customJson = Json { prettyPrint = true }
        val expectedUser =
            TestUser(
                id = 104,
                name = "Bob",
                email = "bob@example.com",
            )

        // Act
        dataStore.putObject(userKey, expectedUser, json = customJson)
        val actualUser = dataStore.getObject<TestUser>(userKey, json = customJson).first()

        // Assert
        actualUser shouldBe expectedUser
    }

    test("removeObject should delete serializable data class object from dataStore") {
        // Arrange
        val initialUser =
            TestUser(
                id = 102,
                name = "Jane Doe",
                email = "jane.doe@example.com",
            )
        dataStore.putObject(userKey, initialUser)

        // Act
        dataStore.removeObject(userKey)
        val actualUser = dataStore.getObject<TestUser>(userKey).first()

        // Assert
        actualUser shouldBe null
    }
})
