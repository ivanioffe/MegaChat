package com.ioffeivan.core.domain.base

/**
 * A generic class that holds a value with its status.
 * Used to handle data, business rule violations, and system errors in a unified way.
 * @param D Type of the success data.
 * @param E Type of the business rule error (domain-specific).
 */
sealed class Result<out D, out E> {
    data class Success<out D>(val data: D) : Result<D, Nothing>()

    data class BusinessRuleError<out E>(val businessRuleError: E) : Result<Nothing, E>()

    data class Error(val error: AppError) : Result<Nothing, Nothing>()
}

suspend fun <T, E> Result<T, E>.onSuccess(
    block: suspend (data: T) -> Unit,
): Result<T, E> =
    apply {
        if (this is Result.Success<T>) {
            block(data)
        }
    }

suspend fun <T, E> Result<T, E>.onBusinessRuleError(
    block: suspend (error: E) -> Unit,
): Result<T, E> =
    apply {
        if (this is Result.BusinessRuleError) {
            block(businessRuleError)
        }
    }

suspend fun <T, E> Result<T, E>.onError(
    block: suspend (error: AppError) -> Unit,
): Result<T, E> =
    apply {
        if (this is Result.Error) {
            block(error)
        }
    }
