package com.ioffeivan.megachat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ioffeivan.feature.auth.presentation.email_verification.navigation.EmailVerificationRoute
import com.ioffeivan.feature.auth.presentation.sign_in.navigation.SignInRoute
import com.ioffeivan.feature.onboarding.presentation.navigation.OnboardingRoute
import com.ioffeivan.megachat.MainActivityState

@Composable
fun AppNavGraph(
    state: MainActivityState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val rootStartDestination =
        if (state is MainActivityState.LoggedIn) AuthorizedRoute else UnauthorizedRoute
    val unauthorizedStartDestination =
        when (state) {
            is MainActivityState.NeedsVerification -> EmailVerificationRoute(state.email)
            MainActivityState.Registered -> SignInRoute
            else -> OnboardingRoute
        }

    NavHost(
        navController = navController,
        startDestination = rootStartDestination,
        modifier = modifier,
    ) {
        unauthorized(
            startDestination = unauthorizedStartDestination,
            navController = navController,
        )

        authorized(
            navController = navController,
        )
    }
}
