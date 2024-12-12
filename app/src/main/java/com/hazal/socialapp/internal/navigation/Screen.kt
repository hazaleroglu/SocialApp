package com.hazal.socialapp.internal.navigation

import androidx.navigation.NamedNavArgument

enum class AllScreens {
    LOGIN,
    HOME,
    REGISTER,
    FORGOT_PASSWORD,
    PROFILE
}

sealed class Screen(
    val route: String,
    val navArgument: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen(
        route = AllScreens.HOME.name
    ) {
        fun createRoute() = AllScreens.HOME.name
    }

    data object Profile : Screen(
        route = AllScreens.PROFILE.name
    ) {
        fun createRoute() = AllScreens.PROFILE.name
    }

    data object Login : Screen(
        route = AllScreens.LOGIN.name
    ) {
        fun createRoute() = AllScreens.LOGIN.name
    }

    data object Register : Screen(
        route = AllScreens.REGISTER.name
    ) {
        fun createRoute() = AllScreens.REGISTER.name
    }

    data object ForgotPassword : Screen(
        route = AllScreens.FORGOT_PASSWORD.name
    ) {
        fun createRoute() = AllScreens.FORGOT_PASSWORD.name
    }

}