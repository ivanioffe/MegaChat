package com.ioffeivan.feature.auth.domain.repository

import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.feature.auth.domain.model.AuthCredentials
import com.ioffeivan.feature.auth.domain.model.AuthStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedIn: Flow<AuthStatus>

    suspend fun signUp(authCredentials: AuthCredentials): Result<Unit, SignUp>

    suspend fun signIn(authCredentials: AuthCredentials): Result<Unit, SignIn>

    sealed class SignUp {
        data object UserExists : SignUp()
    }

    sealed class SignIn {
        data object InvalidCredentials : SignIn()
    }
}
