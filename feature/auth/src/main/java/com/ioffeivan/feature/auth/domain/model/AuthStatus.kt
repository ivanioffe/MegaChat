package com.ioffeivan.feature.auth.domain.model

sealed class AuthStatus {
    data class LoggedOut(val isUserRegistered: Boolean) : AuthStatus()

    data class NeedsVerification(val email: String?) : AuthStatus()

    data object LoggedIn : AuthStatus()
}
