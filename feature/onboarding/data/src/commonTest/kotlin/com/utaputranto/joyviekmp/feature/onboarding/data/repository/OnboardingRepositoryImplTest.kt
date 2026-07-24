package com.utaputranto.joyviekmp.feature.onboarding.data.repository

import com.utaputranto.joyviekmp.core.test.fakes.FakePreferenceStorage
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import kotlinx.coroutines.flow.first
import io.kotest.matchers.shouldBe
import joyviekmp.feature.onboarding.data.generated.resources.Res

class OnboardingRepositoryImplTest : FunSpec({

    lateinit var fakePreferenceStorage: FakePreferenceStorage
    lateinit var repository: OnboardingRepositoryImpl

    beforeTest {
        fakePreferenceStorage = FakePreferenceStorage()
        repository = OnboardingRepositoryImpl(fakePreferenceStorage)
    }

    test("isCompleted should return value from preferenceStorage") {
        withData(
            nameFn = { "when preference is $it then should return $it" },
            true,
            false,
        ) { expectedValue ->
            // Arrange
            fakePreferenceStorage.completeOnboarding(expectedValue)

            // Act
            val actual = repository.isCompleted()

            // Assert
            actual shouldBe expectedValue
        }
    }

    test("completeOnboarding should save true to preferenceStorage") {
        // Arrange
        val expectedValue = true
        fakePreferenceStorage.completeOnboarding(false) // Initial value

        // Act
        repository.completeOnboarding()

        // Assert
        fakePreferenceStorage.isCompletedOnboarding().first() shouldBe expectedValue
    }

    test("getOnboardingPages should return valid 3 pages") {
        // Act
        val pages = repository.getOnboardingPages()

        // Assert
        pages.size shouldBe 3

        pages[0].id shouldBe 1
        pages[0].title shouldBe Res.string.onboarding_step1_title
        pages[0].imageBackground shouldBe Res.drawable.ic_onboarding_step1_trending

        pages[1].id shouldBe 2
        pages[1].title shouldBe Res.string.onboarding_step2_title
        pages[1].imageBackground shouldBe Res.drawable.ic_onboarding_step2_search

        pages[2].id shouldBe 3
        pages[2].title shouldBe Res.string.onboarding_step3_title
        pages[2].imageBackground shouldBe Res.drawable.ic_onboarding_step3_watchlist
    }
})
