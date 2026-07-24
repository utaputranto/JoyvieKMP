package com.utaputranto.joyviekmp.feature.onboarding.presentation

import app.cash.turbine.test
import com.utaputranto.joyviekmp.core.test.fakes.FakeOnboardingRepository
import com.utaputranto.joyviekmp.core.test.rules.MainDispatcherRule
import com.utaputranto.joyviekmp.feature.onboarding.domain.model.OnboardingPage
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.GetIsCompletedOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.GetOnboardingPagesUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

@OptIn(ExperimentalResourceApi::class, InternalResourceApi::class)
class OnboardingViewModelTest :
    BehaviorSpec({
        val mainDispatcherRule = MainDispatcherRule()

        lateinit var fakeRepository: FakeOnboardingRepository
        lateinit var completeOnboardingUseCase: CompleteOnboardingUseCase
        lateinit var getOnboardingPagesUseCase: GetOnboardingPagesUseCase
        lateinit var getIsCompletedOnboardingUseCase: GetIsCompletedOnboardingUseCase
        lateinit var viewModel: OnboardingViewModel

        beforeSpec {
            mainDispatcherRule.starting()
        }

        afterSpec {
            mainDispatcherRule.finished()
        }

        afterTest {
            stopKoin()
        }

        Given("the view model is initialized with uncompleted onboarding") {
            fakeRepository = FakeOnboardingRepository()
            completeOnboardingUseCase = CompleteOnboardingUseCase(fakeRepository)
            getOnboardingPagesUseCase = GetOnboardingPagesUseCase(fakeRepository)
            getIsCompletedOnboardingUseCase = GetIsCompletedOnboardingUseCase(fakeRepository)

            val expectedPages =
                listOf(
                    OnboardingPage(
                        id = 1,
                        title = StringResource("t", "t", emptySet()),
                        titleHighlight = StringResource("t", "t", emptySet()),
                        description = StringResource("d", "d", emptySet()),
                        buttonText = StringResource("b", "b", emptySet()),
                        imageBackground = DrawableResource("i", emptySet()),
                    ),
                )
            fakeRepository.setPages(expectedPages)
            fakeRepository.setCompleted(false)

            viewModel =
                OnboardingViewModel(
                    completeOnboardingUseCase,
                    getOnboardingPagesUseCase,
                    getIsCompletedOnboardingUseCase,
                )

            When("observing pages and toast events") {
                Then("it should expose pages and uncompleted event") {
                    viewModel.pages.test {
                        awaitItem() shouldBe expectedPages
                    }

                    viewModel.toastEvent.test {
                        awaitItem() shouldBe "Onboarding UnCompleted!"
                    }
                }
            }
        }

        Given("the view model is initialized with completed onboarding") {
            fakeRepository = FakeOnboardingRepository()
            completeOnboardingUseCase = CompleteOnboardingUseCase(fakeRepository)
            getOnboardingPagesUseCase = GetOnboardingPagesUseCase(fakeRepository)
            getIsCompletedOnboardingUseCase = GetIsCompletedOnboardingUseCase(fakeRepository)

            fakeRepository.setCompleted(true)
            viewModel =
                OnboardingViewModel(
                    completeOnboardingUseCase,
                    getOnboardingPagesUseCase,
                    getIsCompletedOnboardingUseCase,
                )

            When("observing toast events") {
                Then("it should send Onboarding Completed! event") {
                    viewModel.toastEvent.test {
                        awaitItem() shouldBe "Onboarding Completed!"
                    }
                }
            }
        }

        Given("the view model is created") {
            fakeRepository = FakeOnboardingRepository()
            completeOnboardingUseCase = CompleteOnboardingUseCase(fakeRepository)
            getOnboardingPagesUseCase = GetOnboardingPagesUseCase(fakeRepository)
            getIsCompletedOnboardingUseCase = GetIsCompletedOnboardingUseCase(fakeRepository)

            fakeRepository.setCompleted(false)
            viewModel =
                OnboardingViewModel(
                    completeOnboardingUseCase,
                    getOnboardingPagesUseCase,
                    getIsCompletedOnboardingUseCase,
                )

            When("finishOnboarding is called") {
                var callbackCalled = false
                viewModel.finishOnboarding {
                    callbackCalled = true
                }

                mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

                Then("repository should be updated and callback called") {
                    fakeRepository.isCompleted() shouldBe true
                    callbackCalled shouldBe true
                }
            }
        }
    }),
    KoinTest
