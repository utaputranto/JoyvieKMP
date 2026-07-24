package com.utaputranto.joyviekmp.feature.onboarding.domain.usecase

import com.utaputranto.joyviekmp.core.test.fakes.FakeOnboardingRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GetIsCompletedOnboardingUseCaseTest : FunSpec({

    lateinit var fakeRepository: FakeOnboardingRepository
    lateinit var useCase: GetIsCompletedOnboardingUseCase

    beforeTest {
        fakeRepository = FakeOnboardingRepository()
        useCase = GetIsCompletedOnboardingUseCase(fakeRepository)
    }

    test("invoke should return true when repository is completed") {
        // Arrange
        fakeRepository.setCompleted(true)

        // Act
        val result = useCase()

        // Assert
        result shouldBe true
    }

    test("invoke should return false when repository is not completed") {
        // Arrange
        fakeRepository.setCompleted(false)

        // Act
        val result = useCase()

        // Assert
        result shouldBe false
    }
})
