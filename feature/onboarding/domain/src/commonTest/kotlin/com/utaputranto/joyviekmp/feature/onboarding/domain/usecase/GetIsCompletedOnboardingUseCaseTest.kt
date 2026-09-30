package com.utaputranto.joyviekmp.feature.onboarding.domain.usecase

import com.utaputranto.joyviekmp.feature.onboarding.domain.fakes.FakeOnboardingRepository
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
        fakeRepository.setCompleted(true)

        val result = useCase()

        result shouldBe true
    }

    test("invoke should return false when repository is not completed") {
        fakeRepository.setCompleted(false)

        val result = useCase()

        result shouldBe false
    }
})
