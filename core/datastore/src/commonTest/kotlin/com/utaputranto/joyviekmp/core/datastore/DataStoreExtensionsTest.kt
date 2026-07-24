package com.utaputranto.joyviekmp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
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
