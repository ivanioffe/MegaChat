package com.ioffeivan.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private const val DEBOUNCE_TIME_MILLIS_DEFAULT = 500L

/**
 * Creates a debounced wrapper around the provided [onClick] lambda.
 *
 * This function prevents the execution of [onClick] if it is called too quickly
 * within the specified [debounceTimeMillis]. It ensures that the action is only
 * triggered if the elapsed time since the last successful execution meets or
 * exceeds the debounce period.
 *
 * @param debounceTimeMillis The minimum time in milliseconds that must pass
 * between consecutive successful calls to the returned lambda.
 * Defaults to [DEBOUNCE_TIME_MILLIS_DEFAULT].
 * @param onClick The original action lambda to be executed when the debounce period has passed.
 * @return A new lambda [() -> Unit] that wraps [onClick] with debounce logic.
 */
@Composable
fun onDebounceClick(
    debounceTimeMillis: Long = DEBOUNCE_TIME_MILLIS_DEFAULT,
    onClick: () -> Unit,
): () -> Unit {
    var lastClickTimeMillis by remember { mutableLongStateOf(0L) }

    return {
        System.currentTimeMillis().let { currentTimeMillis ->
            if (currentTimeMillis - lastClickTimeMillis >= debounceTimeMillis) {
                lastClickTimeMillis = currentTimeMillis
                onClick()
            }
        }
    }
}
