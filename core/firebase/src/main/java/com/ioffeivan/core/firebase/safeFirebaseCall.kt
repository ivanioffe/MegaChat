package com.ioffeivan.core.firebase

import com.google.firebase.FirebaseException
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.core.firebase.error.FirebaseError
import com.ioffeivan.core.firebase.error.mapToFirebaseError
import com.ioffeivan.core.firebase.error.toAppError

suspend fun <T, E> safeFirebaseCall(
    call: suspend () -> T,
    onError: (FirebaseError) -> E? = { null },
): Result<T, E> {
    return try {
        Result.Success(call())
    } catch (e: Exception) {
        val firebaseError = (e as? FirebaseException)?.mapToFirebaseError() ?: FirebaseError.Unknown
        val specificError = onError(firebaseError)

        if (specificError != null) {
            Result.BusinessRuleError(specificError)
        } else {
            Result.Error(firebaseError.toAppError())
        }
    }
}
