package com.ioffeivan.feature.auth.presentation.email_verification.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ioffeivan.feature.auth.presentation.email_verification.EmailVerificationViewModel
import com.ioffeivan.feature.auth.presentation.email_verification.composable.EmailVerificationRoute
import kotlinx.serialization.Serializable

@Serializable
data class EmailVerificationRoute(val email: String?)

fun NavGraphBuilder.emailVerification() {
    composable<EmailVerificationRoute> { backStackEntry ->
        val (email) = backStackEntry.toRoute<EmailVerificationRoute>()
        val creationCallback: (EmailVerificationViewModel.Factory) -> EmailVerificationViewModel =
            { factory ->
                factory.create(email)
            }

        EmailVerificationRoute(
            viewModel =
                hiltViewModel(
                    key = email,
                    creationCallback = creationCallback,
                ),
        )
    }
}
