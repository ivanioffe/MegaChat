package com.ioffeivan.megachat.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ioffeivan.megachat.MainActivityState
import com.ioffeivan.megachat.navigation.AppNavGraph

@Composable
fun AppScreen(
    state: MainActivityState,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    AppNavGraph(
        navController = navController,
        state = state,
        modifier = modifier,
    )
}
