package com.ioffeivan.core.mvu

import com.google.common.truth.Truth.assertThat

fun <State, Command, Effect> ReducerResult<State, Command, Effect>.assertMatches(
    expectedState: State,
    expectedCommand: Command? = null,
    expectedEffect: Effect? = null,
) {
    assertThat(this.state).isEqualTo(expectedState)
    assertThat(this.command).isEqualTo(expectedCommand)
    assertThat(this.effect).isEqualTo(expectedEffect)
}
