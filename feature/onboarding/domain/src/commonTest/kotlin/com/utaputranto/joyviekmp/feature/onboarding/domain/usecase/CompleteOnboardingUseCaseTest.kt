package com.utaputranto.joyviekmp.feature.onboarding.domain.usecase

import com.utaputranto.joyviekmp.core.test.fakes.FakeOnboardingRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CompleteOnboardingUseCaseTest : FunSpec({

    lateinit var fakeRepository: FakeOnboardingRepository
    lateinit var useCase: CompleteOnboardingUseCase

    beforeTest {
        fakeRepository = FakeOnboardingRepository()
        useCase = CompleteOnboardingUseCase(fakeRepository)
    }

    test("invoke should complete onboarding in repository") {
        // Arrange
        fakeRepository.setCompleted(false)

        // Act
        useCase()

        // Assert
        fakeRepository.isCompleted() shouldBe true
    }
})
