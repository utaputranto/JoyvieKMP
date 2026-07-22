package com.utaputranto.joyviekmp.core.mvi

import app.cash.turbine.test
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

private data class TestState(val count: Int = 0) : UiState

private sealed interface TestEvent : UiEvent {
    data object Increment : TestEvent

    data object TriggerEffect : TestEvent
}

private sealed interface TestEffect : UiEffect {
    data object Toast : TestEffect
}

private class ConcreteStateMachine : BaseStateMachine<TestState, TestEvent, TestEffect>(TestState()) {
    fun getSnapshot(): TestState = currentState

    override fun onEvent(event: TestEvent) {
        when (event) {
            TestEvent.Increment -> setState { copy(count = currentState.count + 1) }
            TestEvent.TriggerEffect -> sendEffect(TestEffect.Toast)
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class BaseStateMachineTest : BehaviorSpec({
    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    Given("a BaseStateMachine implementation") {
        lateinit var stateMachine: ConcreteStateMachine

        beforeTest {
            stateMachine = ConcreteStateMachine()
        }

        When("currentState is accessed") {
            Then("it should return the current state snapshot") {
                stateMachine.getSnapshot() shouldBe TestState(count = 0)
            }
        }

        When("an event reducing state is dispatched") {
            Then("the state should update correctly") {
                stateMachine.onEvent(TestEvent.Increment)
                testDispatcher.scheduler.advanceUntilIdle()
                stateMachine.state.value.count shouldBe 1
                stateMachine.getSnapshot().count shouldBe 1
            }
        }

        When("an event emitting a side effect is dispatched") {
            Then("the effect flow should emit the effect") {
                stateMachine.effect.test {
                    stateMachine.onEvent(TestEvent.TriggerEffect)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem() shouldBe TestEffect.Toast
                }
            }
        }
    }
})
