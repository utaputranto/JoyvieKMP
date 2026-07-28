package com.utaputranto.joyviekmp.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Marker for immutable UI state. */
interface UiState

/** Marker for user/UI intents fed into [BaseStateMachine.onEvent]. */
interface UiEvent

/** Marker for one-shot side effects (navigation, toast, ...). */
interface UiEffect

/**
 * Reusable MVI base for feature ViewModels.
 *
 * Holds a single [state] [StateFlow] and a one-shot [effect] [Flow]. Subclasses reduce state with
 * [setState] and emit side effects with [sendEffect], and route intents through [onEvent].
 *
 * @param S immutable state type
 * @param E intent type
 * @param F one-shot effect type
 */
abstract class BaseStateMachine<S : UiState, E : UiEvent, F : UiEffect>(
    initialState: S,
) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = Channel<F>(Channel.BUFFERED)
    val effect: Flow<F> = _effect.receiveAsFlow()

    /** Current state snapshot. */
    protected val currentState: S
        get() = _state.value

    /** Handle an incoming intent. */
    abstract fun onEvent(event: E)

    /** Reduce [currentState] into a new state. */
    protected fun setState(reducer: S.() -> S) {
        _state.update(reducer)
    }

    /** Emit a one-shot side effect. */
    protected fun sendEffect(effect: F) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
