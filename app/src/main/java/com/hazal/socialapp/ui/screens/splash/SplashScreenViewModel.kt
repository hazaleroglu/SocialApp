package com.hazal.socialapp.ui.screens.splash

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.ViewModel
import com.hazal.socialapp.internal.navigation.AllScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SplashScreenViewModel() : ViewModel() {

    private val _onContinue = MutableStateFlow(false)
    val onContinue: StateFlow<Boolean> = _onContinue

    private val _startDestination = MutableStateFlow(AllScreens.LOGIN.name)
    val startDestination: StateFlow<String> = _startDestination.asStateFlow()

    @Composable
    fun RequestLocationPermissions() {
        val context = LocalContext.current

        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsGranted ->
            if (permissionsGranted.containsValue(false) || permissionsGranted.isEmpty()) {
                Toast.makeText(context, "Location permissions are required", Toast.LENGTH_SHORT).show()
                openAppSettings(context)
            }
        }

        LaunchedEffect(Unit) {
            if (permissions.all { ContextCompat.checkSelfPermission(context, it) == PermissionChecker.PERMISSION_GRANTED }) {
                _onContinue.value = true
            } else {
                permissionLauncher.launch(permissions)
            }

        }
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }
}