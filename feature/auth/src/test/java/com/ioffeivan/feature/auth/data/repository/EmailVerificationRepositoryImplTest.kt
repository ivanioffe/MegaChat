package com.ioffeivan.feature.auth.data.repository

import com.google.android.gms.tasks.Task
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.feature.auth.domain.repository.EmailVerificationRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EmailVerificationRepositoryImplTest {
    private val firebaseAuth = mockk<FirebaseAuth>()
    private lateinit var emailVerificationRepository: EmailVerificationRepositoryImpl

    private val task = mockk<Task<Void>>()
    private val user = mockk<FirebaseUser>()

    @BeforeEach
    fun setUp() {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        emailVerificationRepository = EmailVerificationRepositoryImpl(firebaseAuth)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun whenSendEmailVerificationIsSuccess_shouldReturnSuccess() =
        runTest {
            val expected = Result.Success(Unit)

            every { firebaseAuth.currentUser } returns user
            every { user.sendEmailVerification() } returns task
            coEvery { task.await() } returns mockk()

            val actual = emailVerificationRepository.sendEmailVerification()

            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun whenCheckVerificationIsSuccessAndEmailIsVerified_shouldSignOutAndReturnSuccess() =
        runTest {
            val expected = Result.Success(Unit)

            every { firebaseAuth.currentUser } returns user
            every { user.reload() } returns task
            coEvery { task.await() } returns mockk()
            every { user.isEmailVerified } returns true
            every { firebaseAuth.signOut() } just runs

            val actual = emailVerificationRepository.checkEmailVerification()

            assertThat(actual).isEqualTo(expected)
            verify(exactly = 1) { firebaseAuth.signOut() }
        }

    @Test
    fun whenCheckVerificationIsSuccessButEmailIsNotVerified_shouldReturnNoEmailVerificationError() =
        runTest {
            val expected =
                Result.BusinessRuleError(
                    EmailVerificationRepository.CheckEmailVerificationError.NoEmailVerification,
                )

            every { firebaseAuth.currentUser } returns user
            every { user.reload() } returns task
            coEvery { task.await() } returns mockk()
            every { user.isEmailVerified } returns false

            val actual = emailVerificationRepository.checkEmailVerification()

            assertThat(actual).isEqualTo(expected)
            verify(exactly = 0) { firebaseAuth.signOut() }
        }
}
