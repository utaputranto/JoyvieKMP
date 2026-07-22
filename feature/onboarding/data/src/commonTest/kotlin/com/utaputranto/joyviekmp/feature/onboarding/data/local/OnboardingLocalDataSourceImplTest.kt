package com.utaputranto.joyviekmp.feature.onboarding.data.local

import com.utaputranto.joyviekmp.feature.onboarding.data.fakes.FakeDataStoreManager
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first

class OnboardingLocalDataSourceImplTest : BehaviorSpec({

    lateinit var fakeDataStoreManager: FakeDataStoreManager
    lateinit var dataSource: OnboardingLocalDataSourceImpl

    beforeTest {
        fakeDataStoreManager = FakeDataStoreManager()
        dataSource = OnboardingLocalDataSourceImpl(fakeDataStoreManager)
    }

    Given("OnboardingLocalDataSourceImpl") {
        When("initial state is checked") {
            Then("isCompletedOnboarding should default to false") {
                dataSource.isCompletedOnboarding().first() shouldBe false
            }
        }

        When("completeOnboarding is called with true") {
            Then("isCompletedOnboarding should emit true") {
                dataSource.completeOnboarding(true)
                dataSource.isCompletedOnboarding().first() shouldBe true
            }
        }

        When("completeOnboarding is called with false") {
            Then("isCompletedOnboarding should emit false") {
                dataSource.completeOnboarding(true)
                dataSource.completeOnboarding(false)
                dataSource.isCompletedOnboarding().first() shouldBe false
            }
        }
    }
})
