package com.ioffeivan.feature.auth.presentation.sign_in.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ioffeivan.feature.auth.presentation.sign_in.composable.SignInRoute
import kotlinx.serialization.Serializable

@Serializable
data object SignInRoute

fun NavController.navigateToSignIn(navOptions: NavOptions? = null) =
    navigate(SignInRoute, navOptions)

fun NavGraphBuilder.signIn(
    onNavigateBack: () -> Unit,
) {
    composable<SignInRoute> {
        SignInRoute(
            onNavigateBack = onNavigateBack,
        )
    }
}
