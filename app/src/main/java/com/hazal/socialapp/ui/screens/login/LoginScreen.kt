package com.hazal.socialapp.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hazal.socialapp.internal.components.EmailField
import com.hazal.socialapp.internal.components.PasswordField
import com.hazal.socialapp.internal.components.SocialAppDialogBox
import com.hazal.socialapp.internal.components.SocialAppDialogBoxModel
import com.hazal.socialapp.internal.components.SocialAppLoadingView
import com.hazal.socialapp.internal.components.SocialAppRoundedCheckBox
import com.hazal.socialapp.internal.components.SocialAppSwitchButton
import com.hazal.socialapp.internal.navigation.Screen
import com.hazal.socialapp.internal.ext.isValidEmail

@Composable
fun LoginScreen(
    navigate: (String) -> Unit,
    navigateAndPopUp: (String, String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dialogState by viewModel.dialogState

    var passwordVisible by remember { mutableStateOf(false) }
    var isRememberMeChecked by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isRememberCheckboxChecked) {
        isRememberMeChecked = uiState.isRememberCheckboxChecked
    }

    Content(
        uiState = uiState,
        dialogState = dialogState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordVisible = passwordVisible,
        onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
        isRememberMeChecked = isRememberMeChecked,
        onRememberMeCheckboxClick = { isRememberMeChecked = !isRememberMeChecked },
        onLoginClick = {
            viewModel.onLoginClick(navigateAndPopUp, isRememberMeChecked)
        },
        onSignUpClick = { navigate.invoke(Screen.Register.route) },
        onForgotPasswordClick = { navigate.invoke("") },
        onPositiveDialogButtonClick = { viewModel.closeDialog() }
    )

}

@Composable
private fun Content(
    uiState: LoginUiState,
    dialogState: SocialAppDialogBoxModel?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isRememberMeChecked: Boolean,
    onRememberMeCheckboxClick: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onPositiveDialogButtonClick: () -> Unit
) {
    SocialAppLoadingView(isLoading = uiState.loading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 30.dp),
                text = "Email",
                textAlign = TextAlign.Start,
                fontSize = 15.sp
            )
            EmailField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(10.dp),
                value = uiState.email,
                onNewValue = onEmailChange
            )
            SocialAppRoundedCheckBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                label = "We can use animations to make it behave similar to the default",
                isChecked = uiState.email.isValidEmail()
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 30.dp),
                text = "Password",
                textAlign = TextAlign.Start,
                fontSize = 15.sp
            )
            PasswordField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp),
                value = uiState.password,
                shape = RoundedCornerShape(10.dp),
                onNewValue = onPasswordChange,
                isVisible = onPasswordVisible,
                visibilityClick = onTogglePasswordVisibility
            )
            Row(
                modifier = Modifier.padding(top = 10.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialAppSwitchButton(
                    checked = isRememberMeChecked,
                    onCheckedChange = onRememberMeCheckboxClick
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "Remember Me"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Forgot password?",
                    color = Color.Blue,
                    textAlign = TextAlign.End,
                    fontSize = 15.sp
                )
            }
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(top = 50.dp, start = 30.dp, end = 30.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White,
                    disabledContentColor = Color.Green,
                    disabledContainerColor = Color.Green
                ),
                onClick = onLoginClick
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Log In",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Text(
                    text = "Don't have an account?"
                )
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onSignUpClick.invoke()
                        }, text = "Sign Up"
                )
            }
        }
        if (dialogState != null) {
            SocialAppDialogBox(
                onDismissRequest = { },
                content = SocialAppDialogBoxModel(
                    mainColor = dialogState.mainColor,
                    title = dialogState.title,
                    description = dialogState.description,
                    positiveButtonText = "Ok"
                ),
                positiveButtonClickAction = onPositiveDialogButtonClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    Content(
        LoginUiState("", "", false, false),
        null,
        {},
        {},
        false,
        {},
        false,
        {},
        {},
        {},
        {},
        {}
    )
}