package com.ioffeivan.feature.auth.domain.usecase

import com.ioffeivan.feature.auth.domain.model.AuthStatus
import com.ioffeivan.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAuthStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Flow<AuthStatus> = authRepository.isLoggedIn
}
