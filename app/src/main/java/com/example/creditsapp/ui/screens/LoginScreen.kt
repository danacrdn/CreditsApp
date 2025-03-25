package com.example.creditsapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import com.example.creditsapp.R
import com.example.creditsapp.ui.theme.CreditsAppTheme
import com.example.creditsapp.viewmodel.LoginViewModel
import kotlin.math.log

@Composable
fun LoginScreen(modifier: Modifier = Modifier, loginViewModel: LoginViewModel){

    val email : String by loginViewModel.email.observeAsState("")
    val password : String by loginViewModel.password.observeAsState("")
    val loginEnabled : Boolean by loginViewModel.loginEnabled.observeAsState(false)

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ){
        LoginImage()
        LoginText()
        Spacer(modifier = Modifier.height(20.dp))
        EmailTextField(email) { loginViewModel.onLoginChanged(email = it, password = password) }
        Spacer(modifier = Modifier.height(10.dp))
        PasswordTextField(password){ loginViewModel.onLoginChanged(email = email, password = it) }
        Spacer(modifier = Modifier.height(20.dp))
        LoginButton(loginEnable = loginEnabled)
        Spacer(modifier = Modifier.height(20.dp))
        ForgotPasswordText()
        Spacer(modifier = Modifier.height(50.dp))
        SignupButton()
    }
}

@Composable
fun SignupButton(modifier: Modifier = Modifier) {
    OutlinedButton (
        onClick = {},
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
        modifier = Modifier.clickable {  }
    )
}

@Composable
fun LoginButton(modifier: Modifier = Modifier, loginEnable: Boolean) {
    Button(onClick = {},
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
        style = MaterialTheme.typography.displayMedium
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
fun LoginScreenPreview(){
    CreditsAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            //LoginScreen(modifier = Modifier.fillMaxSize())
        }
    }
}


