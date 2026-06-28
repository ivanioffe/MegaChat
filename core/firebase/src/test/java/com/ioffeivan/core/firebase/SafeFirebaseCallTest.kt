package com.ioffeivan.core.firebase

import com.google.common.truth.Truth.assertThat
import com.ioffeivan.core.domain.base.AppError
import com.ioffeivan.core.domain.base.Result
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SafeFirebaseCallTest {
    @Test
    fun whenCallSucceeds_shouldReturnSuccess() =
        runTest {
            val data = "data"
            val expected = Result.Success(data)

            val actual =
                safeFirebaseCall<String, Nothing>(
                    call = { data },
                )

            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun whenCallThrowsExceptionAndOnErrorMapsIt_shouldReturnBusinessRuleError() =
        runTest {
            val businessRuleError = "businessRuleError"
            val expected = Result.BusinessRuleError(businessRuleError)

            val actual =
                safeFirebaseCall<String, String>(
                    call = { throw Exception() },
                    onError = { businessRuleError },
                )

            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun whenCallThrowsExceptionAndOnErrorReturnsNull_shouldMapToAppErrorAndReturnError() =
        runTest {
            val expected = Result.Error(AppError.Unknown)

            val actual =
                safeFirebaseCall(
                    call = { throw Exception() },
                    onError = { null },
                )

            assertThat(actual).isEqualTo(expected)
        }
}
