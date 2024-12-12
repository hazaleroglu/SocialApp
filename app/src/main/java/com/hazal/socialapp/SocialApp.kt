package com.hazal.socialapp

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.hazal.socialapp.internal.navigation.Screen
import com.hazal.socialapp.ui.screens.home.HomeScreen
import com.hazal.socialapp.ui.screens.login.LoginScreen
import com.hazal.socialapp.ui.theme.SocialAppTheme

@Composable
fun SocialApp(startDestination: String) {
    SocialAppTheme {
        val socialAppNavController = rememberSocialAppNavController()

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NavHost(
                navController = socialAppNavController.navController,
                startDestination = startDestination,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                socialAppNavGraph(socialAppNavController)
            }
        }
    }
}

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val LOGIN_ROUTE = "login"
    const val ONBOARDING_ROUTE = "onboarding"
}

@Composable
fun rememberSocialAppNavController(
    navController: NavHostController = rememberNavController(),
) = remember(navController) {
    SocialAppNavController(navController = navController)
}

private fun NavGraphBuilder.socialAppNavGraph(
    socialAppNavController: SocialAppNavController
) {
    navigation(
        route = MainDestinations.LOGIN_ROUTE,
        startDestination = Screen.Login.route
    ) {
        addLoginGraph(socialAppNavController)
    }

    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = Screen.Home.route
    ) {
        addHomeGraph()
    }
}

fun NavGraphBuilder.addLoginGraph(
    socialAppNavController: SocialAppNavController
) {
    composable(
        Screen.Login.route
    ) {
        LoginScreen(
            navigate = { route ->
                socialAppNavController.navigate(route)
            },
            navigateAndPopUp = { route, popupName ->
                socialAppNavController.navigateAndPopUp(route, popupName)
            }
        )
    }
}

fun NavGraphBuilder.addHomeGraph() {
    composable(Screen.Home.route) {
        HomeScreen()
    }
}