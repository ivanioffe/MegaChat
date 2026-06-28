package com.ioffeivan.core.domain.base

sealed interface AppError {
    data object NoNetwork : AppError

    data object ServiceUnavailable : AppError

    data object TooManyRequests : AppError

    data object Unknown : AppError
}
