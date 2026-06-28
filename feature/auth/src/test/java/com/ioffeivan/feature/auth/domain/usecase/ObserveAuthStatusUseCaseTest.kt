package com.ioffeivan.feature.auth.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ioffeivan.feature.auth.domain.model.AuthStatus
import com.ioffeivan.feature.auth.domain.repository.AuthRepository
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ObserveAuthStatusUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var observeAuthStatusUseCase: ObserveAuthStatusUseCase

    @BeforeEach
    fun setUp() {
        authRepository = mockk()
        observeAuthStatusUseCase = ObserveAuthStatusUseCase(authRepository)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @ParameterizedTest
    @MethodSource("authStatuses")
    fun whenRepositoryReturnsAuthStatus_shouldReturnSameAuthStatus(expected: AuthStatus) =
        runTest {
            every { authRepository.isLoggedIn } returns flowOf(expected)

            observeAuthStatusUseCase().test {
                val actual = awaitItem()
                assertThat(actual).isEqualTo(expected)

                awaitComplete()
            }

            verify(exactly = 1) { authRepository.isLoggedIn }
        }

    companion object {
        @JvmStatic
        fun authStatuses(): Stream<Arguments> =
            Stream.of(
                Arguments.of(AuthStatus.LoggedOut(isUserRegistered = false)),
                Arguments.of(AuthStatus.LoggedOut(isUserRegistered = true)),
                Arguments.of(AuthStatus.NeedsVerification("email@email.com")),
                Arguments.of(AuthStatus.NeedsVerification(null)),
                Arguments.of(AuthStatus.LoggedIn),
            )
    }
}
