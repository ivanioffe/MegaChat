package com.ioffeivan.feature.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.ioffeivan.feature.auth.presentation.email_verification.navigation.emailVerification
import com.ioffeivan.feature.auth.presentation.sign_in.navigation.signIn
import com.ioffeivan.feature.auth.presentation.sign_up.navigation.signUp

fun NavGraphBuilder.auth(
    navController: NavHostController,
) {
    signUp(
        onNavigateBack = navController::popBackStack,
    )

    signIn(
        onNavigateBack = navController::popBackStack,
    )

    emailVerification()
}
