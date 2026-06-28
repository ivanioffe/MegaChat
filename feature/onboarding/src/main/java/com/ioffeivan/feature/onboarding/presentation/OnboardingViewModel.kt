package com.ioffeivan.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import com.ioffeivan.core.auth.GoogleAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class OnboardingViewModel @Inject constructor(
    val googleAuthManager: GoogleAuthManager,
) : ViewModel()
