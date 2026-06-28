package com.ioffeivan.megachat.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

@Serializable
data object AuthorizedRoute

fun NavGraphBuilder.authorized(
    navController: NavHostController,
) {
}
