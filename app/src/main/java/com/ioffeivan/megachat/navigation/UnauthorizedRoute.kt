package com.ioffeivan.megachat.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.ioffeivan.feature.auth.presentation.navigation.auth
import com.ioffeivan.feature.auth.presentation.sign_in.navigation.navigateToSignIn
import com.ioffeivan.feature.auth.presentation.sign_up.navigation.navigateToSignUp
import com.ioffeivan.feature.onboarding.presentation.navigation.onboarding
import kotlinx.serialization.Serializable

@Serializable
data object UnauthorizedRoute

fun NavGraphBuilder.unauthorized(
    startDestination: Any,
    navController: NavHostController,
) {
    navigation<UnauthorizedRoute>(
        startDestination = startDestination,
    ) {
        onboarding(
            onNavigateToSignIn = navController::navigateToSignIn,
            onNavigateToSignUp = navController::navigateToSignUp,
        )

        auth(navController = navController)
    }
}
