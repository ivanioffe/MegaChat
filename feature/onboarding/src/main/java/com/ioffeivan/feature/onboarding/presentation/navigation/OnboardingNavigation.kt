package com.ioffeivan.feature.onboarding.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ioffeivan.feature.onboarding.presentation.composable.OnboardingRoute
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingRoute

fun NavGraphBuilder.onboarding(
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit,
) {
    composable<OnboardingRoute> {
        OnboardingRoute(
            onNavigateToSignIn = onNavigateToSignIn,
            onNavigateToSignUp = onNavigateToSignUp,
        )
    }
}
