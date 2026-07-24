package com.utaputranto.joyviekmp.feature.onboarding.data.repository

import com.utaputranto.joyviekmp.feature.onboarding.data.fakes.FakeOnboardingLocalDataSource
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

class OnboardingRepositoryImplTest : FunSpec({

    lateinit var fakeLocalDataSource: FakeOnboardingLocalDataSource
    lateinit var repository: OnboardingRepositoryImpl

    beforeTest {
        fakeLocalDataSource = FakeOnboardingLocalDataSource()
        repository = OnboardingRepositoryImpl(fakeLocalDataSource)
    }

    test("isCompleted should return true when preference is completed") {
        fakeLocalDataSource.completeOnboarding(true)
        val actual = repository.isCompleted()
        actual shouldBe true
    }

    test("isCompleted should return false when preference is not completed") {
        fakeLocalDataSource.completeOnboarding(false)
        val actual = repository.isCompleted()
        actual shouldBe false
    }

    test("completeOnboarding should save true to preferenceStorage") {
        val expectedValue = true
        fakeLocalDataSource.completeOnboarding(false)

        repository.completeOnboarding()

        fakeLocalDataSource.isCompletedOnboarding().first() shouldBe expectedValue
    }

    test("getOnboardingPages should return valid 3 pages") {
        val pages = repository.getOnboardingPages()

        pages.size shouldBe 3
        pages[0].id shouldBe 1
        pages[1].id shouldBe 2
        pages[2].id shouldBe 3
    }
})
