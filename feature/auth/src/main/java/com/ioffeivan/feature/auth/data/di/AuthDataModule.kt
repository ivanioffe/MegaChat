package com.ioffeivan.feature.auth.data.di

import com.ioffeivan.feature.auth.data.repository.AuthRepositoryImpl
import com.ioffeivan.feature.auth.data.repository.EmailVerificationRepositoryImpl
import com.ioffeivan.feature.auth.domain.repository.AuthRepository
import com.ioffeivan.feature.auth.domain.repository.EmailVerificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface AuthDataModule {
    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindEmailVerificationRepository(impl: EmailVerificationRepositoryImpl): EmailVerificationRepository
}
