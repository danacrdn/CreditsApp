package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.AppViewModelProvider
import com.example.creditsapp.R
import com.example.creditsapp.presentation.components.TopBar
import com.example.creditsapp.presentation.navigation.Screen
import com.example.creditsapp.presentation.viewmodel.ProfileEditEvent
import com.example.creditsapp.presentation.viewmodel.ProfileViewModel
import com.example.creditsapp.presentation.viewmodel.SessionViewModel
import com.example.creditsapp.ui.theme.CreditsAppTheme

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
    sessionViewModel: SessionViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val isDarkMode by sessionViewModel.isDarkMode.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    /* Launched effect that listens the flow of navigation from the viewmodel.
    When viewmodel emits true to navigateToLogin, then navigates to login screen.
    It cleans the stack from the last session.
    * */
    LaunchedEffect(viewModel.navigateToLogin) {
        viewModel.navigateToLogin.collect { navigate ->
            if (navigate) {
                navController.navigate(Screen.Login.name) {
                    popUpTo(navController.graph.id) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    Scaffold(
        topBar = { TopBar(R.string.profile, navigateBack = { navController.popBackStack() }) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                uiState.profileData?.let { ProfilePictureAndName(it.nombre) }

                Spacer(modifier = Modifier.height(20.dp))


                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SectionText(title = stringResource(R.string.personal_information))
                        Spacer(modifier = Modifier.weight(1f))

                        if (!uiState.isEditing) {
                            IconButton(onClick = { viewModel.startEditing() }) {
                                Icon(
                                    imageVector = Icons.Rounded.Edit,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primaryContainer
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    uiState.profileData?.let {
                        ProfileOption(
                            title = stringResource(R.string.name),
                            isEditing = uiState.isEditing,
                            value = (if (uiState.isEditing) uiState.editableProfileData.nombre else it.nombre),
                            onValueChanged = { viewModel.onEvent(ProfileEditEvent.NombreChanged(it))}
                        )
                    }
                    uiState.profileData?.let {
                        ProfileOption(
                            title = stringResource(R.string.last_name),
                            isEditing = uiState.isEditing,
                            value = (if (uiState.isEditing) uiState.editableProfileData.apellido else it.apellido),
                            onValueChanged = { viewModel.onEvent(ProfileEditEvent.ApellidoChanged(it))}
                        )
                    }
                    uiState.profileData?.let {
                        ProfileOption(
                            title = stringResource(R.string.no_control),
                            isEditing = uiState.isEditing,
                            value = it.numeroControl,
                            onValueChanged = { /* No se edita */}
                        )
                    }

                    uiState.profileData?.let {
                        ProfileOption(
                            title = stringResource(R.string.email),
                            isEditing = uiState.isEditing,
                            value = (if (uiState.isEditing) uiState.editableProfileData.correoElectronico else it.correoElectronico),
                            onValueChanged = { viewModel.onEvent(ProfileEditEvent.EmailChanged(it)) }
                        )
                    }
                    uiState.profileData?.let {
                        ProfileOption(
                            title = stringResource(R.string.semester),
                            isEditing = uiState.isEditing,
                            value = "",
                            onValueChanged = {  }
                        )
                    }

                    val selectedCarrera = if (uiState.isEditing) {
                        uiState.editableProfileData.carrera
                    } else {
                        uiState.carreras.find { it.nombre == uiState.profileData?.carreraNombre }
                    }

                    selectedCarrera?.let { carrera ->
                        DropdownOption(
                            title = stringResource(R.string.degree_name),
                            options = uiState.carreras,
                            isEditing = uiState.isEditing,
                            selectedOption = carrera,
                            onOptionSelected = { selected ->
                                viewModel.onEvent(ProfileEditEvent.CarreraChanged(selected))
                            },
                            optionLabel = { it.nombre }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column {
                    SectionText(title = stringResource(R.string.settings))
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileOption(
                        title = stringResource(R.string.language), value = stringResource(
                            R.string.spanish
                        ),
                        isEditing = uiState.isEditing,
                        onValueChanged = {}
                    )
                    ProfileSwitchOption(
                        title = stringResource(R.string.dark_mode), value = isDarkMode,
                        onCheckedValue = { sessionViewModel.switchDarkMode() }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column {
                    SectionText(title = stringResource(R.string.security))
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileOptionPassword(
                        title = stringResource(R.string.password),
                        isEditing = uiState.isEditing,
                        value = (if (uiState.isEditing) uiState.editableProfileData.currentPassword else "*****"),
                        onValueChanged = { viewModel.onEvent(ProfileEditEvent.PasswordChanged(it)) }
                    )
                    ProfileOptionPassword(
                        title = stringResource(R.string.confirm_password),
                        isEditing = uiState.isEditing,
                        value = (if (uiState.isEditing) uiState.editableProfileData.newPassword else "*****"),
                        onValueChanged = { viewModel.onEvent(ProfileEditEvent.ConfirmPasswordChanged(it)) }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (!uiState.isEditing) {
                    LogOutOption(onClick = { viewModel.logOut() })
                } else {
                    Row {
                        SaveButton { viewModel.saveNewData() }
                        Spacer(modifier = Modifier.width(16.dp))
                        CancelButton { viewModel.cancelEditing() }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownOption(
    title: String,
    isEditing: Boolean,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f)
        )
        if (isEditing) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {
                OutlinedTextField(
                    value = selectedOption?.let { optionLabel(it) } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Selecciona una opción") },
                    shape = RoundedCornerShape(30.dp),
                    singleLine = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(optionLabel(option)) },
                            onClick = {
                                onOptionSelected(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        } else {
            if (selectedOption != null) {
                Text(
                    text = optionLabel(selectedOption),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun CancelButton(onClick: () -> Unit) {
    Button(onClick = onClick, shape = RoundedCornerShape(30.dp)) {
        Text(text = stringResource(R.string.cancel))
    }
}

@Composable
fun SaveButton(onClick: () -> Unit) {
    Button(onClick = onClick, shape = RoundedCornerShape(30.dp)) {
        Text(text = stringResource(R.string.save))
    }
}

@Composable
fun LogOutOption(onClick: () -> Unit) {
    Text(
        text = stringResource(R.string.log_out),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.clickable { onClick() }
    )
}


@Composable
fun SectionText(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun ProfileOption(
    title: String,
    value: String,
    isEditing: Boolean,
    onValueChanged: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f)
        )
        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChanged,
                placeholder = { Text(text = title) },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ProfileOptionPassword(
    title: String,
    value: String,
    isEditing: Boolean,
    onValueChanged: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f)
        )
        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChanged,
                placeholder = { Text(text = title) },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                maxLines = 1,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ProfileSwitchOption(title: String, value: Boolean, onCheckedValue: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title, style = MaterialTheme.typography.titleSmall)
        Switch(
            checked = value,
            onCheckedChange = onCheckedValue,
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }

}

@Composable
fun ProfilePictureAndName(
    name: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.size(60.dp)) {
            Image(
                painter = painterResource(R.drawable.default_icon),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    CreditsAppTheme {
        //ProfileScreen()
    }
}