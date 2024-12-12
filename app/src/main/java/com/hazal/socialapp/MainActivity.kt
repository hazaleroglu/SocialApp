package com.hazal.socialapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hazal.socialapp.ui.screens.splash.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocialAppContent(
                splashViewModel = splashViewModel,
                onAppReady = { destination ->
                    SocialApp(
                        startDestination = destination
                    )
                })
        }
    }
}

@Composable
fun SocialAppContent(
    splashViewModel: SplashScreenViewModel,
    onAppReady: @Composable (String) -> Unit,
) {
    val startDestination by splashViewModel.startDestination.collectAsState()
    val continueState by splashViewModel.onContinue.collectAsState()

    splashViewModel.RequestLocationPermissions()
    if (continueState) {
        onAppReady(startDestination)
    }

}