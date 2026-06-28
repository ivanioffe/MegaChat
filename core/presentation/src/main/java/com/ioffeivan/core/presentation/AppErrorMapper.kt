package com.ioffeivan.core.presentation

import androidx.annotation.StringRes
import com.ioffeivan.core.domain.base.AppError

@StringRes
fun AppError.asStringRes(): Int {
    return when (this) {
        AppError.NoNetwork -> R.string.error_no_internet
        AppError.ServiceUnavailable -> R.string.error_service_unavailable
        AppError.TooManyRequests -> R.string.error_too_many_requests
        AppError.Unknown -> R.string.error_unknown
    }
}
