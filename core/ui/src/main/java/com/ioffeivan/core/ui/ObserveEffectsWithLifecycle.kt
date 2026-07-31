package com.ioffeivan.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

/**
 * Collects [effects] from a Flow and processes them via [onEffect] safely tied to the Android
 * lifecycle, typically for handling one-time UI events(Effects).
 *
 * The collection is active only when the [lifecycleOwner] is in or above [minActiveState].
 *
 * @param effects The Kotlin Flow emitting side effects.
 * @param onEffect The suspended lambda function to process the received effect.
 * @param lifecycleOwner The LifecycleOwner to observe.
 * @param minActiveState The minimum lifecycle state required for the collection to be active.
 */
@Composable
fun <T> ObserveEffectsWithLifecycle(
    effects: Flow<T>,
    onEffect: suspend (T) -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
) {
    LaunchedEffect(effects, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(state = minActiveState) {
            withContext(Dispatchers.Main.immediate) {
                effects.collectLatest(onEffect)
            }
        }
    }
}
