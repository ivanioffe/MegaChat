package com.ioffeivan.feature.auth.presentation.sign_up.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ioffeivan.feature.auth.presentation.sign_up.composable.SignUpRoute
import kotlinx.serialization.Serializable

@Serializable
data object SignUpRoute

fun NavController.navigateToSignUp(navOptions: NavOptions? = null) =
    navigate(SignUpRoute, navOptions)

fun NavGraphBuilder.signUp(
    onNavigateBack: () -> Unit,
) {
    composable<SignUpRoute> {
        SignUpRoute(
            onNavigateBack = onNavigateBack,
        )
    }
}
