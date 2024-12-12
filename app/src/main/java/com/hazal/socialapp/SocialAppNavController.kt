package com.hazal.socialapp

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController

@Stable
class SocialAppNavController(
    val navController: NavHostController
) {

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }
}