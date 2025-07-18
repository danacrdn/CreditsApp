package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.creditsapp.R
import com.example.creditsapp.presentation.navigation.Screen
import com.example.creditsapp.ui.theme.CreditsAppTheme

@Composable
fun RegisterScreen(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        RegisterText()
        Spacer(modifier = Modifier.height(20.dp))
        DataTextField(
            value = "Nombre",
            onValueChange = {},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            labelText = stringResource(R.string.name)
        )
        Spacer(modifier = Modifier.height(10.dp))
        DataTextField(
            value = "Apellido",
            onValueChange = {},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            labelText = stringResource(R.string.last_name)
        )
        Spacer(modifier = Modifier.height(10.dp))
        DataTextField(
            value = "Número de control",
            onValueChange = {},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            labelText = stringResource(R.string.no_control)
        )
        Spacer(modifier = Modifier.height(10.dp))
        DataTextField(
            value = "Email",
            onValueChange = {},
            KeyboardOptions(keyboardType = KeyboardType.Email),
            stringResource(R.string.email)
        )
        Spacer(modifier = Modifier.height(10.dp))
        DataTextField(
            value = "Semestre",
            onValueChange = {},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            labelText = stringResource(R.string.semester)
        )
        Spacer(modifier = Modifier.height(10.dp))
        DataTextField(
            value = "Carrera",
            onValueChange = {},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            labelText = stringResource(R.string.degree_name)
        )
        Spacer(modifier = Modifier.height(10.dp))
        DataTextField(
            value = "Contraseña",
            onValueChange = {},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            labelText = stringResource(R.string.password)
        )
        Spacer(modifier = Modifier.height(10.dp))
        DataTextField(
            value = "Confirmar contraseña",
            onValueChange = {},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            labelText = stringResource(R.string.password)
        )
        Spacer(modifier = Modifier.height(20.dp))
        ConfirmSignUpButton()
        Spacer(modifier = Modifier.height(10.dp))
        LoginBackButton(navController)
    }
}

@Composable
fun LoginBackButton(navController: NavController) {
    OutlinedButton(
        onClick = { navController.navigate(Screen.Login.name) },
        modifier = Modifier
            .width(325.dp)
            .height(50.dp),
    ) {
        Text(
            text = stringResource(R.string.log_in),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun ConfirmSignUpButton() {
    Button(
        onClick = {  },
        enabled = true,
        modifier = Modifier
            .width(325.dp)
            .height(50.dp),
    ) {
        Text(
            text = stringResource(R.string.sign_up),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun DataTextField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    labelText: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = labelText)
        },
        shape = RoundedCornerShape(30.dp),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier.width(325.dp)
    )
}

@Composable
fun RegisterText() {
    Text(
        text = stringResource(R.string.sign_in),
        style = MaterialTheme.typography.displayMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Preview
@Composable
fun RegisterScreenPreview() {
    CreditsAppTheme {

    }
}
