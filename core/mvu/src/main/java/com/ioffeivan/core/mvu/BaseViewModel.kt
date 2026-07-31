package com.ioffeivan.core.mvu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

open class BaseViewModel<
    State : Reducer.State,
    Action : Reducer.Action,
    Event : Action,
    Message : Action,
    Command : Reducer.Command,
    Effect : Reducer.Effect,
>(
    private val initialState: State,
    private val reducer: Reducer<State, Action, Command, Effect>,
) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    val state: StateFlow<State> by lazy {
        _state
            .onStart {
                viewModelScope.launch {
                    initialDataLoad()
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = initialState,
            )
    }

    private val _effect = Channel<Effect>(capacity = Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val actionMutex = Mutex()

    fun onEvent(event: Event) = processAction(event)

    protected fun onMessage(message: Message) = processAction(message)

    private fun processAction(action: Action) {
        viewModelScope.launch {
            actionMutex.withLock {
                val (newState, command, effect) = reducer.reduce(_state.value, action)

                _state.value = newState
                effect?.let { _effect.send(effect) }
                command?.let { handleCommand(command) }
            }
        }
    }

    protected open fun handleCommand(command: Command) {}

    protected open suspend fun initialDataLoad() {}
}
