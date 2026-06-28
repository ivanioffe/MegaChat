package com.ioffeivan.feature.auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.core.firebase.error.FirebaseError
import com.ioffeivan.core.firebase.safeFirebaseCall
import com.ioffeivan.feature.auth.domain.model.AuthCredentials
import com.ioffeivan.feature.auth.domain.model.AuthStatus
import com.ioffeivan.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {
    private var isUserRegistered: Boolean = false
    override val isLoggedIn: Flow<AuthStatus> =
        callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener {
                    val user = it.currentUser
                    val status =
                        when {
                            user == null -> AuthStatus.LoggedOut(isUserRegistered)

                            !user.isEmailVerified -> {
                                isUserRegistered = true
                                AuthStatus.NeedsVerification(user.email)
                            }

                            else -> {
                                isUserRegistered = false
                                AuthStatus.LoggedIn
                            }
                        }

                    trySend(status)
                }

            firebaseAuth.addAuthStateListener(listener)

            awaitClose { firebaseAuth.removeAuthStateListener(listener) }
        }

    override suspend fun signUp(
        authCredentials: AuthCredentials,
    ): Result<Unit, AuthRepository.SignUp> {
        return safeFirebaseCall(
            call = {
                firebaseAuth.createUserWithEmailAndPassword(
                    authCredentials.email,
                    authCredentials.password,
                ).await()
            },
            onError = { firebaseError ->
                when (firebaseError) {
                    FirebaseError.AuthUserCollision ->
                        AuthRepository.SignUp.UserExists

                    else -> null
                }
            },
        )
    }

    override suspend fun signIn(
        authCredentials: AuthCredentials,
    ): Result<Unit, AuthRepository.SignIn> {
        return safeFirebaseCall(
            call = {
                firebaseAuth.signInWithEmailAndPassword(
                    authCredentials.email,
                    authCredentials.password,
                ).await()
            },
            onError = { firebaseError ->
                when (firebaseError) {
                    FirebaseError.AuthInvalidCredentials ->
                        AuthRepository.SignIn.InvalidCredentials

                    else -> null
                }
            },
        )
    }
}
