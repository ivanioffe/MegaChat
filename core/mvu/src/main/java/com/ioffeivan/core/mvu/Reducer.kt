package com.ioffeivan.core.mvu

interface Reducer<State : Reducer.State, Action : Reducer.Action, Command : Reducer.Command, Effect : Reducer.Effect> {
    interface State

    interface Action

    interface Command

    interface Effect

    fun reduce(
        previousState: State,
        action: Action,
    ): ReducerResult<State, Command, Effect>

    fun result(
        state: State,
        block: ReducerResultBuilder<State, Command, Effect>.() -> Unit,
    ): ReducerResult<State, Command, Effect> {
        return ReducerResultBuilder<State, Command, Effect>(state)
            .apply(block)
            .build()
    }
}

@ConsistentCopyVisibility
data class ReducerResult<State, Command, Effect> internal constructor(
    val state: State,
    val command: Command? = null,
    val effect: Effect? = null,
)

class ReducerResultBuilder<State, Command, Effect> internal constructor(
    initialState: State,
) {
    private var state: State = initialState
    var command: Command? = null
    var effect: Effect? = null

    fun state(block: State.() -> State) {
        state = state.block()
    }

    internal fun build(): ReducerResult<State, Command, Effect> {
        return ReducerResult(
            state = state,
            command = command,
            effect = effect,
        )
    }
}
