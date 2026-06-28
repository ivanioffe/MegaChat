package com.ioffeivan.megachat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ioffeivan.feature.auth.domain.model.AuthStatus
import com.ioffeivan.feature.auth.domain.usecase.ObserveAuthStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    observeAuthStatusUseCase: ObserveAuthStatusUseCase,
) : ViewModel() {
    val state =
        observeAuthStatusUseCase()
            .map { authStatus ->
                when (authStatus) {
                    is AuthStatus.LoggedOut -> {
                        if (authStatus.isUserRegistered) {
                            MainActivityState.Registered
                        } else {
                            MainActivityState.LoggedOut
                        }
                    }

                    is AuthStatus.NeedsVerification -> MainActivityState.NeedsVerification(authStatus.email)
                    AuthStatus.LoggedIn -> MainActivityState.LoggedIn
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = MainActivityState.Loading,
            )
}

sealed class MainActivityState {
    data object Loading : MainActivityState()

    data object LoggedOut : MainActivityState()

    data class NeedsVerification(val email: String?) : MainActivityState()

    data object Registered : MainActivityState()

    data object LoggedIn : MainActivityState()

    fun shouldKeepSplashScreen() = this is Loading
}
