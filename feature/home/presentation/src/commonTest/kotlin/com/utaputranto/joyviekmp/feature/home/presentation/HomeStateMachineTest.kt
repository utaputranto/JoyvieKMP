package com.utaputranto.joyviekmp.feature.home.presentation

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.core.test.rules.MainDispatcherRule
import com.utaputranto.joyviekmp.feature.home.domain.usecase.GetNowPlayingMoviesUseCase
import com.utaputranto.joyviekmp.feature.home.domain.usecase.GetTopRatedMoviesUseCase
import com.utaputranto.joyviekmp.feature.home.domain.usecase.GetUpcomingMoviesUseCase
import com.utaputranto.joyviekmp.feature.home.presentation.fakes.FakeHomeRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class HomeStateMachineTest : FunSpec({
    val mainDispatcherRule = MainDispatcherRule()

    beforeSpec { mainDispatcherRule.starting() }
    afterSpec { mainDispatcherRule.finished() }

    val sampleMovie =
        Movie(
            id = 1,
            title = "Sample Movie",
            overview = "Overview",
            posterPath = "/sample.jpg",
            releaseDate = "2026-01-01",
            voteAverage = 7.5,
        )

    fun buildStateMachine(fakeRepository: FakeHomeRepository): HomeStateMachine =
        HomeStateMachine(
            GetNowPlayingMoviesUseCase(fakeRepository),
            GetTopRatedMoviesUseCase(fakeRepository),
            GetUpcomingMoviesUseCase(fakeRepository),
        )

    test("HomeState defaults every section to Loading and selectedTab to Home") {
        val state = HomeState()

        state.selectedTab shouldBe HomeTab.Home
        state.nowPlaying shouldBe SectionUiState.Loading
        state.latest shouldBe SectionUiState.Loading
        state.upcoming shouldBe SectionUiState.Loading
    }

    test("init loads all three sections to Success independently") {
        val fakeRepository = FakeHomeRepository()
        fakeRepository.setNowPlayingResult(Result.success(listOf(sampleMovie)))
        fakeRepository.setTopRatedResult(Result.success(listOf(sampleMovie)))
        fakeRepository.setUpcomingResult(Result.success(listOf(sampleMovie)))

        val stateMachine = buildStateMachine(fakeRepository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        stateMachine.state.value.nowPlaying shouldBe SectionUiState.Success(listOf(sampleMovie))
        stateMachine.state.value.latest shouldBe SectionUiState.Success(listOf(sampleMovie))
        stateMachine.state.value.upcoming shouldBe SectionUiState.Success(listOf(sampleMovie))
    }

    test("one section failing sets only that section to Error, others stay Success") {
        val fakeRepository = FakeHomeRepository()
        fakeRepository.setNowPlayingResult(Result.failure(RuntimeException("now playing failed")))
        fakeRepository.setTopRatedResult(Result.success(listOf(sampleMovie)))
        fakeRepository.setUpcomingResult(Result.success(listOf(sampleMovie)))

        val stateMachine = buildStateMachine(fakeRepository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        stateMachine.state.value.nowPlaying shouldBe SectionUiState.Error("now playing failed")
        stateMachine.state.value.latest shouldBe SectionUiState.Success(listOf(sampleMovie))
        stateMachine.state.value.upcoming shouldBe SectionUiState.Success(listOf(sampleMovie))
    }

    test("SelectTab updates selectedTab without re-invoking any use case") {
        val fakeRepository = FakeHomeRepository()
        val stateMachine = buildStateMachine(fakeRepository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val nowPlayingCallsBefore = fakeRepository.nowPlayingCallCount
        val topRatedCallsBefore = fakeRepository.topRatedCallCount
        val upcomingCallsBefore = fakeRepository.upcomingCallCount

        stateMachine.onEvent(HomeEvent.SelectTab(HomeTab.Search))
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        stateMachine.state.value.selectedTab shouldBe HomeTab.Search
        fakeRepository.nowPlayingCallCount shouldBe nowPlayingCallsBefore
        fakeRepository.topRatedCallCount shouldBe topRatedCallsBefore
        fakeRepository.upcomingCallCount shouldBe upcomingCallsBefore
    }

    test("RetryNowPlaying re-invokes only the now playing use case") {
        val fakeRepository = FakeHomeRepository()
        val stateMachine = buildStateMachine(fakeRepository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val nowPlayingCallsBefore = fakeRepository.nowPlayingCallCount
        val topRatedCallsBefore = fakeRepository.topRatedCallCount
        val upcomingCallsBefore = fakeRepository.upcomingCallCount

        stateMachine.onEvent(HomeEvent.RetryNowPlaying)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        fakeRepository.nowPlayingCallCount shouldBe nowPlayingCallsBefore + 1
        fakeRepository.topRatedCallCount shouldBe topRatedCallsBefore
        fakeRepository.upcomingCallCount shouldBe upcomingCallsBefore
    }

    test("RetryLatest re-invokes only the top rated use case") {
        val fakeRepository = FakeHomeRepository()
        val stateMachine = buildStateMachine(fakeRepository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val nowPlayingCallsBefore = fakeRepository.nowPlayingCallCount
        val topRatedCallsBefore = fakeRepository.topRatedCallCount
        val upcomingCallsBefore = fakeRepository.upcomingCallCount

        stateMachine.onEvent(HomeEvent.RetryLatest)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        fakeRepository.topRatedCallCount shouldBe topRatedCallsBefore + 1
        fakeRepository.nowPlayingCallCount shouldBe nowPlayingCallsBefore
        fakeRepository.upcomingCallCount shouldBe upcomingCallsBefore
    }

    test("RetryUpcoming re-invokes only the upcoming use case") {
        val fakeRepository = FakeHomeRepository()
        val stateMachine = buildStateMachine(fakeRepository)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()
        val nowPlayingCallsBefore = fakeRepository.nowPlayingCallCount
        val topRatedCallsBefore = fakeRepository.topRatedCallCount
        val upcomingCallsBefore = fakeRepository.upcomingCallCount

        stateMachine.onEvent(HomeEvent.RetryUpcoming)
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        fakeRepository.upcomingCallCount shouldBe upcomingCallsBefore + 1
        fakeRepository.nowPlayingCallCount shouldBe nowPlayingCallsBefore
        fakeRepository.topRatedCallCount shouldBe topRatedCallsBefore
    }
})
