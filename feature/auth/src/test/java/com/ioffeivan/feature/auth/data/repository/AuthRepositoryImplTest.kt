package com.ioffeivan.feature.auth.data.repository

import app.cash.turbine.test
import com.google.android.gms.tasks.Task
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.ioffeivan.core.domain.base.Result
import com.ioffeivan.feature.auth.domain.model.AuthCredentials
import com.ioffeivan.feature.auth.domain.model.AuthStatus
import com.ioffeivan.feature.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AuthRepositoryImplTest {
    private val firebaseAuth = mockk<FirebaseAuth>()
    private lateinit var authRepository: AuthRepositoryImpl

    @BeforeEach
    fun setUp() {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        authRepository = AuthRepositoryImpl(firebaseAuth)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Nested
    inner class IsLoggedIn {
        private val listenerSlot = slot<FirebaseAuth.AuthStateListener>()

        @BeforeEach
        fun setUpFlow() {
            every { firebaseAuth.addAuthStateListener(capture(listenerSlot)) } answers {}
        }

        @Test
        fun whenUserIsNull_shouldEmitLoggedOutWithIsRegisteredFalse() =
            runTest {
                authRepository.isLoggedIn.test {
                    every { firebaseAuth.currentUser } returns null
                    listenerSlot.captured.onAuthStateChanged(firebaseAuth)

                    assertThat(awaitItem()).isEqualTo(AuthStatus.LoggedOut(isUserRegistered = false))

                    cancelAndIgnoreRemainingEvents()
                }
            }

        @Test
        fun whenUserIsNullAfterNeedingVerification_shouldEmitLoggedOutWithIsRegisteredTrue() =
            runTest {
                val expectedEmail = "email@email.com"
                val user =
                    mockk<FirebaseUser> {
                        every { isEmailVerified } returns false
                        every { email } returns expectedEmail
                    }

                authRepository.isLoggedIn.test {
                    every { firebaseAuth.currentUser } returns user
                    listenerSlot.captured.onAuthStateChanged(firebaseAuth)
                    assertThat(awaitItem()).isEqualTo(AuthStatus.NeedsVerification(expectedEmail))

                    every { firebaseAuth.currentUser } returns null
                    listenerSlot.captured.onAuthStateChanged(firebaseAuth)
                    assertThat(awaitItem()).isEqualTo(AuthStatus.LoggedOut(isUserRegistered = true))

                    cancelAndIgnoreRemainingEvents()
                }
            }

        @Test
        fun whenUserExistsButEmailNotVerified_shouldEmitNeedsVerification() =
            runTest {
                val expectedEmail = "email@email.com"
                val user =
                    mockk<FirebaseUser> {
                        every { isEmailVerified } returns false
                        every { email } returns expectedEmail
                    }

                authRepository.isLoggedIn.test {
                    every { firebaseAuth.currentUser } returns user
                    listenerSlot.captured.onAuthStateChanged(firebaseAuth)

                    assertThat(awaitItem()).isEqualTo(AuthStatus.NeedsVerification(expectedEmail))

                    cancelAndIgnoreRemainingEvents()
                }
            }

        @Test
        fun whenUserExistsAndEmailVerified_shouldEmitLoggedIn() =
            runTest {
                val user =
                    mockk<FirebaseUser> {
                        every { isEmailVerified } returns true
                    }

                authRepository.isLoggedIn.test {
                    every { firebaseAuth.currentUser } returns user
                    listenerSlot.captured.onAuthStateChanged(firebaseAuth)

                    assertThat(awaitItem()).isEqualTo(AuthStatus.LoggedIn)

                    cancelAndIgnoreRemainingEvents()
                }
            }

        @Test
        fun whenFlowIsClosed_shouldRemoveAuthStateListener() {
            runTest {
                authRepository.isLoggedIn.test {
                    cancelAndIgnoreRemainingEvents()
                }

                verify(exactly = 1) { firebaseAuth.removeAuthStateListener(listenerSlot.captured) }
            }
        }
    }

    @Nested
    inner class SignUp {
        private val task = mockk<Task<AuthResult>>()
        private val authCredentials = AuthCredentials("email", "password")

        @Test
        fun whenSignUpIsSuccess_shouldReturnSuccess() =
            runTest {
                val expected = Result.Success(Unit)
                every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns task
                coEvery { task.await() } returns mockk()

                val actual = authRepository.signUp(authCredentials)

                assertThat(actual).isEqualTo(expected)
            }

        @Test
        fun whenUserCollision_shouldReturnUserExistsError() =
            runTest {
                val expected = Result.BusinessRuleError(AuthRepository.SignUp.UserExists)
                val exception = mockk<FirebaseAuthUserCollisionException>()
                every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns task
                coEvery { task.await() } throws exception

                val actual = authRepository.signUp(authCredentials)

                assertThat(actual).isEqualTo(expected)
            }
    }

    @Nested
    inner class SignIn {
        private val task = mockk<Task<AuthResult>>()
        private val authCredentials = AuthCredentials("email", "password")

        @Test
        fun whenSignInIsSuccess_shouldReturnSuccess() =
            runTest {
                val expected = Result.Success(Unit)
                every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns task
                coEvery { task.await() } returns mockk()

                val actual = authRepository.signIn(authCredentials)

                assertThat(actual).isEqualTo(expected)
            }

        @Test
        fun whenAuthInvalidCredentials_shouldReturnInvalidCredentials() =
            runTest {
                val expected = Result.BusinessRuleError(AuthRepository.SignIn.InvalidCredentials)
                val exception = mockk<FirebaseAuthInvalidCredentialsException>()
                every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns task
                coEvery { task.await() } throws exception

                val actual = authRepository.signIn(authCredentials)

                assertThat(actual).isEqualTo(expected)
            }
    }
}
