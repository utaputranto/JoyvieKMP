package com.utaputranto.joyviekmp.feature.onboarding.presentation

import app.cash.turbine.test
import com.utaputranto.joyviekmp.core.test.rules.MainDispatcherRule
import com.utaputranto.joyviekmp.feature.onboarding.domain.model.OnboardingPage
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.GetIsCompletedOnboardingUseCase
import com.utaputranto.joyviekmp.feature.onboarding.domain.usecase.GetOnboardingPagesUseCase
import com.utaputranto.joyviekmp.feature.onboarding.presentation.fakes.FakeOnboardingRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

@OptIn(ExperimentalResourceApi::class, InternalResourceApi::class)
class OnboardingStateMachineTest :
    BehaviorSpec({
        val mainDispatcherRule = MainDispatcherRule()

        lateinit var fakeRepository: FakeOnboardingRepository
        lateinit var completeOnboardingUseCase: CompleteOnboardingUseCase
        lateinit var getOnboardingPagesUseCase: GetOnboardingPagesUseCase
        lateinit var getIsCompletedOnboardingUseCase: GetIsCompletedOnboardingUseCase
        lateinit var viewModel: OnboardingStateMachine

        beforeSpec {
            mainDispatcherRule.starting()
        }

        afterSpec {
            mainDispatcherRule.finished()
        }

        afterTest {
            stopKoin()
        }

        Given("onboarding status check when completed") {
            fakeRepository = FakeOnboardingRepository()
            fakeRepository.setCompleted(true)
            completeOnboardingUseCase = CompleteOnboardingUseCase(fakeRepository)
            getOnboardingPagesUseCase = GetOnboardingPagesUseCase(fakeRepository)
            getIsCompletedOnboardingUseCase = GetIsCompletedOnboardingUseCase(fakeRepository)

            When("OnboardingStateMachine is initialized") {
                Then("it should emit NavigateToHome effect") {
                    viewModel =
                        OnboardingStateMachine(
                            completeOnboardingUseCase,
                            getOnboardingPagesUseCase,
                            getIsCompletedOnboardingUseCase,
                        )
                    viewModel.effect.test {
                        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
                        awaitItem() shouldBe OnboardingEffect.NavigateToHome
                    }
                }
            }
        }

        Given("onboarding status check when uncompleted") {
            fakeRepository = FakeOnboardingRepository()
            fakeRepository.setCompleted(false)
            completeOnboardingUseCase = CompleteOnboardingUseCase(fakeRepository)
            getOnboardingPagesUseCase = GetOnboardingPagesUseCase(fakeRepository)
            getIsCompletedOnboardingUseCase = GetIsCompletedOnboardingUseCase(fakeRepository)

            When("OnboardingStateMachine is initialized") {
                Then("it should emit NavigateToWelcome effect") {
                    viewModel =
                        OnboardingStateMachine(
                            completeOnboardingUseCase,
                            getOnboardingPagesUseCase,
                            getIsCompletedOnboardingUseCase,
                        )
                    viewModel.effect.test {
                        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
                        awaitItem() shouldBe OnboardingEffect.NavigateToWelcome
                    }
                }
            }
        }

        Given("the view model is initialized for loading pages") {
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

            viewModel =
                OnboardingStateMachine(
                    completeOnboardingUseCase,
                    getOnboardingPagesUseCase,
                    getIsCompletedOnboardingUseCase,
                )

            When("LoadPages event is sent") {
                Then("it should expose pages in state") {
                    viewModel.onEvent(OnboardingEvent.LoadPages)
                    mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
                    viewModel.state.value.pages shouldBe expectedPages
                }
            }
        }

        Given("the view model completes onboarding") {
            fakeRepository = FakeOnboardingRepository()
            completeOnboardingUseCase = CompleteOnboardingUseCase(fakeRepository)
            getOnboardingPagesUseCase = GetOnboardingPagesUseCase(fakeRepository)
            getIsCompletedOnboardingUseCase = GetIsCompletedOnboardingUseCase(fakeRepository)

            When("FinishOnboarding event is sent") {
                Then("repository should be updated and NavigateToHome effect emitted") {
                    viewModel =
                        OnboardingStateMachine(
                            completeOnboardingUseCase,
                            getOnboardingPagesUseCase,
                            getIsCompletedOnboardingUseCase,
                        )
                    viewModel.effect.test {
                        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
                        awaitItem() shouldBe OnboardingEffect.NavigateToWelcome

                        viewModel.onEvent(OnboardingEvent.FinishOnboarding)
                        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
                        awaitItem() shouldBe OnboardingEffect.NavigateToHome
                    }
                    fakeRepository.isCompleted() shouldBe true
                }
            }
        }
    }),
    KoinTest
