package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.AppViewModelProvider
import com.example.creditsapp.R
import com.example.creditsapp.presentation.navigation.Screen
import com.example.creditsapp.presentation.viewmodel.LoginFormEvent
import com.example.creditsapp.presentation.viewmodel.LoginUiMessageEvent
import com.example.creditsapp.presentation.viewmodel.LoginValidationErrorType
import com.example.creditsapp.presentation.viewmodel.LoginViewModel
import com.example.creditsapp.ui.theme.CreditsAppTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val emptyUserMsg = stringResource(R.string.error_empty_username)
    val emptyPassMsg = stringResource(R.string.error_incorrect_password)
    val failedLoginMsg = stringResource(R.string.login_failed)
    val successLoginMsg = stringResource(R.string.success_login)

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { event ->
            val text = when (event) {
                is LoginUiMessageEvent.ValidationError -> when (event.type) {
                    LoginValidationErrorType.EMPTY_EMAIL -> emptyUserMsg
                    LoginValidationErrorType.EMPTY_PASSWORD -> emptyPassMsg
                }

                LoginUiMessageEvent.LoginFailed -> failedLoginMsg
                LoginUiMessageEvent.LoginSuccess -> successLoginMsg
            }
            snackbarHostState.showSnackbar(text)
        }
    }

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            navController.navigate(Screen.Home.name) {
                popUpTo(Screen.Login.name) { inclusive = true }
            }
            viewModel.resetLoginSuccess()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)

            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                LoginImage()
                LoginText()
                Spacer(modifier = Modifier.height(20.dp))
                EmailTextField(uiState.username) {
                    viewModel.onEvent(
                        LoginFormEvent.UsernameChanged(
                            it
                        )
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextField(uiState.password) {
                    viewModel.onEvent(
                        LoginFormEvent.PasswordChanged(
                            it
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                LoginButton(
                    validateCredentials = { viewModel.logIn() }
                )
                Spacer(modifier = Modifier.height(20.dp))
                ForgotPasswordText()
                Spacer(modifier = Modifier.height(50.dp))
                SignupButton(navController)
            }
        }
    }
}

@Composable
fun SignupButton(navController: NavController, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = { navController.navigate(Screen.Register.name) },
        modifier = modifier
            .width(325.dp)
            .height(50.dp),
    ) {
        Text(
            text = stringResource(R.string.sign_up),
            style = MaterialTheme.typography.labelLarge,
        )
    }

}

@Composable
fun ForgotPasswordText() {
    Text(
        text = stringResource(R.string.forgot_password),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.clickable { }
    )
}

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    validateCredentials: () -> Unit
) {
    Button(
        onClick = { validateCredentials() },
        enabled = true,
        modifier = modifier
            .width(325.dp)
            .height(50.dp),
    ) {
        Text(
            text = stringResource(R.string.log_in),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun PasswordTextField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.labelMedium,
        label = {
            Text(text = stringResource(R.string.password))
        },
        visualTransformation = PasswordVisualTransformation(),
        shape = RoundedCornerShape(30.dp),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.width(325.dp)
    )
}

@Composable
fun EmailTextField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.labelMedium,
        label = {
            Text(text = stringResource(R.string.email))
        },
        shape = RoundedCornerShape(30.dp),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier.width(325.dp)
    )
}

@Composable
fun LoginText() {
    Text(
        text = stringResource(R.string.welcome_text),
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun LoginImage() {
    Image(
        painter = painterResource(R.drawable.login),
        contentDescription = null,
        modifier = Modifier.size(350.dp)
    )
}

@Preview
@Composable
fun LoginScreenPreview() {
    CreditsAppTheme {

    }
}


