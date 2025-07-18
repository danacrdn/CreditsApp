package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.creditsapp.ui.theme.CreditsAppTheme
import com.example.creditsapp.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController
) {

    val email: String by viewModel.email.observeAsState("")
    val password: String by viewModel.password.observeAsState("")
    val loginEnabled: Boolean by viewModel.loginEnabled.observeAsState(false)
    var errorMessage by remember { mutableStateOf("") }


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
        EmailTextField(email) { viewModel.onLoginChanged(email = it, password = password) }
        Spacer(modifier = Modifier.height(10.dp))
        PasswordTextField(password) { viewModel.onLoginChanged(email = email, password = it) }
        Spacer(modifier = Modifier.height(20.dp))
        LoginButton(
            loginEnable = loginEnabled,
            validateCredentials = {
                viewModel.validateCredentials(email, password) { isValid ->
                    if (isValid) {
                        navController.navigate(Screen.Home.name) {
                            popUpTo(Screen.Login.name) { inclusive = true }
                        }
                    } else {
                        errorMessage = "Usuario o contraseña incorrectos"
                    }
                }
            }
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        ForgotPasswordText()
        Spacer(modifier = Modifier.height(50.dp))
        SignupButton(navController)
    }
}

@Composable
fun SignupButton(navController: NavController, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = { navController.navigate(Screen.Register.name)},
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
    loginEnable: Boolean,
    validateCredentials: () -> Unit
) {
    Button(
        onClick = { validateCredentials() },
        enabled = loginEnable,
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


