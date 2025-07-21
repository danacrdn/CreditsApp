package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.AppViewModelProvider
import com.example.creditsapp.R
import com.example.creditsapp.domain.model.Carrera
import com.example.creditsapp.presentation.navigation.Screen
import com.example.creditsapp.presentation.viewmodel.RegisterFormEvent
import com.example.creditsapp.presentation.viewmodel.RegisterViewModel
import com.example.creditsapp.presentation.viewmodel.UiMessageEvent
import com.example.creditsapp.presentation.viewmodel.ValidationErrorType
import com.example.creditsapp.ui.theme.CreditsAppTheme

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val emptyNameMsg = stringResource(R.string.error_empty_name)
    val emptyEmailMsg = stringResource(R.string.error_empty_email)
    val emptyNumControlMsg = stringResource(R.string.error_empty_num)
    val invalidEmailMsg = stringResource(R.string.error_invalid_email)
    val invalidSemesterMsg = stringResource(R.string.error_invalid_semester)
    val invalidCareerMsg = stringResource(R.string.error_invalid_career)
    val invalidPasswordMsg = stringResource(R.string.error_invalid_password)
    val mismatchPasswordMsg = stringResource(R.string.error_mismatch_password)
    val regSuccessMsg = stringResource(R.string.registration_success)
    val regFailedMsg  = stringResource(R.string.registration_failed)

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { event ->
            val text = when (event) {
                is UiMessageEvent.ValidationError -> when (event.type) {
                    ValidationErrorType.EMPTY_NAME -> emptyNameMsg
                    ValidationErrorType.EMPTY_EMAIL -> emptyEmailMsg
                    ValidationErrorType.EMPTY_NOCONTROL -> emptyNumControlMsg
                    ValidationErrorType.INVALID_EMAIL -> invalidEmailMsg
                    ValidationErrorType.INVALID_SEMESTER -> invalidSemesterMsg
                    ValidationErrorType.INVALID_CAREER -> invalidCareerMsg
                    ValidationErrorType.EMPTY_PASSWORD -> invalidPasswordMsg
                    ValidationErrorType.PASSWORD_MISMATCH -> mismatchPasswordMsg
                }
                is UiMessageEvent.RegistrationSuccess -> regSuccessMsg
                is UiMessageEvent.RegistrationFailed -> regFailedMsg
            }
            snackbarHostState.showSnackbar(text)
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
                RegisterText()
                Spacer(modifier = Modifier.height(20.dp))
                DataTextField(
                    value = uiState.nombre,
                    onValueChange = { viewModel.onEvent(RegisterFormEvent.NombreChanged(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    labelText = stringResource(R.string.name)
                )
                Spacer(modifier = Modifier.height(10.dp))
                DataTextField(
                    value = uiState.apellido,
                    onValueChange = { viewModel.onEvent(RegisterFormEvent.ApellidoChanged(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    labelText = stringResource(R.string.last_name)
                )
                Spacer(modifier = Modifier.height(10.dp))
                DataTextField(
                    value = uiState.numeroControl,
                    onValueChange = { viewModel.onEvent(RegisterFormEvent.NumeroControlChanged(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    labelText = stringResource(R.string.no_control)
                )
                Spacer(modifier = Modifier.height(10.dp))
                DataTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onEvent(RegisterFormEvent.EmailChanged(it)) },
                    KeyboardOptions(keyboardType = KeyboardType.Email),
                    stringResource(R.string.email)
                )
                Spacer(modifier = Modifier.height(10.dp))
                DropdownSemester(
                    selectedSemester = uiState.semestre,
                    onSemesterSelected = { viewModel.onEvent(RegisterFormEvent.SemestreChanged(it)) }
                )
                Spacer(modifier = Modifier.height(10.dp))
                DropdownCareers(
                    options = uiState.carreras,
                    selectedCareer = uiState.selectedCareer,
                    onCareerSelected = { viewModel.onEvent(RegisterFormEvent.CareerSelected(it)) }
                )
                Spacer(modifier = Modifier.height(10.dp))
                DataTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onEvent(RegisterFormEvent.PasswordChanged(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    labelText = stringResource(R.string.password),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(10.dp))
                DataTextField(
                    value = uiState.confirmPassword,
                    onValueChange = { viewModel.onEvent(RegisterFormEvent.ConfirmPasswordChanged(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    labelText = stringResource(R.string.confirm_password),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(20.dp))
                ConfirmSignUpButton(viewModel::signUpNewUser, uiState.isRegistering)
                Spacer(modifier = Modifier.height(10.dp))
                LoginBackButton(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSemester(selectedSemester: Int?, onSemesterSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val semesterOptions = (1..12).toList()
    val selectedText = selectedSemester?.toString() ?: "Selecciona tu semestre"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Semestre") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .width(325.dp),
            shape = RoundedCornerShape(30.dp),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            semesterOptions.forEach { semester ->
                DropdownMenuItem(
                    text = { Text(text = semester.toString()) },
                    onClick = {
                        onSemesterSelected(semester)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownCareers(
    options: List<Carrera>,
    selectedCareer: Carrera?,
    onCareerSelected: (Carrera) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedOptionText = selectedCareer?.nombre ?: "Selecciona una carrera"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(325.dp)
    ) {
        OutlinedTextField(
            value = selectedOptionText,
            onValueChange = { /* No permitir edición manual */ },
            readOnly = true,
            label = { Text("Carrera") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .width(325.dp),
            shape = RoundedCornerShape(30.dp),
            singleLine = true,
            maxLines = 1,
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.nombre) },
                    onClick = {
                        onCareerSelected(option)
                        expanded = false
                    }
                )
            }
        }
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
fun ConfirmSignUpButton(signUpUser: () -> Unit, isRegistering: Boolean) {
    Button(
        onClick = signUpUser,
        enabled = true,
        modifier = Modifier
            .width(325.dp)
            .height(50.dp),
    ) {
        if (isRegistering) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = stringResource(R.string.sign_up),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}


@Composable
fun DataTextField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    labelText: String,
    visualTransformation: VisualTransformation = VisualTransformation.None
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
        keyboardOptions = keyboardOptions,
        modifier = Modifier.width(325.dp),
        visualTransformation = visualTransformation
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
