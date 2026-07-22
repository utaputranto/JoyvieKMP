package com.utaputranto.joyviekmp.feature.onboarding.domain.usecase

import com.utaputranto.joyviekmp.feature.onboarding.domain.fakes.FakeOnboardingRepository
import com.utaputranto.joyviekmp.feature.onboarding.domain.model.OnboardingPage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalResourceApi::class, InternalResourceApi::class)
class GetOnboardingPagesUseCaseTest : FunSpec({

    lateinit var fakeRepository: FakeOnboardingRepository
    lateinit var useCase: GetOnboardingPagesUseCase

    beforeTest {
        fakeRepository = FakeOnboardingRepository()
        useCase = GetOnboardingPagesUseCase(fakeRepository)
    }

    test("invoke should return list of pages from repository") {
        val expectedPages =
            listOf(
                OnboardingPage(
                    id = 1,
                    title = StringResource("t1", "t1", emptySet()),
                    titleHighlight = StringResource("t2", "t2", emptySet()),
                    description = StringResource("d1", "d1", emptySet()),
                    buttonText = StringResource("b1", "b1", emptySet()),
                    imageBackground = DrawableResource("img1", emptySet()),
                ),
            )
        fakeRepository.setPages(expectedPages)

        val actualPages = useCase()

        actualPages shouldBe expectedPages
    }
})
