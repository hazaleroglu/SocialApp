package com.hazal.socialapp.ui.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazal.socialapp.MainDestinations
import com.hazal.socialapp.domain.usecase.SignInUseCase
import com.hazal.socialapp.internal.components.SocialAppDialogBoxModel
import com.hazal.socialapp.internal.util.DataStoreManager
import com.hazal.socialapp.internal.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    val dialogState = mutableStateOf<SocialAppDialogBoxModel?>(value = null)

    private var _registerState = MutableStateFlow(value = "")
    val registerState: StateFlow<String> = _registerState.asStateFlow()

    init {
        showLoading()
        viewModelScope.launch {
            dataStoreManager.apply {
                if (!getRememberedPassword().isNullOrEmpty() && !getRememberedEmail().isNullOrEmpty() && getRememberMeCheckboxStatus()) {
                    _uiState.update {
                        _uiState.value.copy(
                            email = getRememberedEmail() ?: "",
                            password = getRememberedPassword() ?: "",
                            isRememberCheckboxChecked = true
                        )
                    }
                }
                hideLoading()
            }
        }
    }

    fun onEmailChange(newValue: String) {
        _uiState.update { _uiState.value.copy(email = newValue) }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update {
            _uiState.value.copy(password = newValue)
        }
    }

    fun onLoginClick(openAndPopUp: (String, String) -> Unit, isRememberMeChecked: Boolean) {
        viewModelScope.launch {
            showLoading()
            try {
                when (val result = signInUseCase(uiState.value.email, uiState.value.password)) {
                    is NetworkResult.Success -> {
                        dataStoreManager.apply {
                            if (isRememberMeChecked) {
                                saveRememberedEmail(uiState.value.email)
                                saveRememberedPassword(uiState.value.password)
                            } else {
                                clearRememberedEmail()
                                clearRememberedPassword()
                            }
                            saveRememberMeCheckboxStatus(isRememberMeChecked)
                        }
                        openAndPopUp.invoke(
                            MainDestinations.HOME_ROUTE,
                            MainDestinations.LOGIN_ROUTE
                        )
                    }

                    is NetworkResult.Error -> {
                    }

                    is NetworkResult.Exception -> {
                        showDialog(result.e.message.toString())
                    }
                }
            } finally {
                hideLoading()
            }
        }
    }

    private fun showDialog(desc: String) {
        dialogState.value = SocialAppDialogBoxModel(
            mainColor = Color.Red,
            title = "Error",
            description = desc,
            positiveButtonText = "Ok"
        )
    }

    fun closeDialog() {
        dialogState.value = null
    }

    private fun showLoading() = _uiState.update { _uiState.value.copy(loading = true) }
    private fun hideLoading() = _uiState.update { _uiState.value.copy(loading = false) }
}