package com.ioffeivan.core.domain.base

sealed class DataResult<out D, out E> {
    data class Success<out D>(val data: D) : DataResult<D, Nothing>()

    data class Error<out E>(val error: E) : DataResult<Nothing, E>()
}

suspend fun <D, E> DataResult<D, E>.onSuccess(
    block: suspend (data: D) -> Unit,
): DataResult<D, E> =
    apply {
        if (this is DataResult.Success) {
            block(data)
        }
    }

suspend fun <D, E> DataResult<D, E>.onError(
    block: suspend (error: E) -> Unit,
): DataResult<D, E> =
    apply {
        if (this is DataResult.Error) {
            block(error)
        }
    }
