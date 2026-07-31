package com.ioffeivan.feature.auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.core.firebase.safeFirebaseCall
import com.ioffeivan.feature.auth.domain.repository.EmailVerificationRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class EmailVerificationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : EmailVerificationRepository {
    override suspend fun sendEmailVerification(): Result<Unit, Nothing> {
        return safeFirebaseCall(
            call = {
                firebaseAuth.currentUser?.sendEmailVerification()?.await()
            },
        )
    }

    override suspend fun checkEmailVerification(): Result<Unit, EmailVerificationRepository.CheckEmailVerificationError> {
        val reloadResult =
            safeFirebaseCall<Unit, EmailVerificationRepository.CheckEmailVerificationError>(
                call = {
                    firebaseAuth.currentUser?.reload()?.await()
                },
            )

        return when (reloadResult) {
            is Result.Success -> {
                if (firebaseAuth.currentUser?.isEmailVerified == true) {
                    firebaseAuth.signOut()
                    Result.Success(Unit)
                } else {
                    Result.BusinessRuleError(
                        EmailVerificationRepository.CheckEmailVerificationError.NoEmailVerification,
                    )
                }
            }

            is Result.BusinessRuleError -> {
                Result.BusinessRuleError(reloadResult.businessRuleError)
            }

            is Result.Error -> {
                Result.Error(reloadResult.error)
            }
        }
    }
}
