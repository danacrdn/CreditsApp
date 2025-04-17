package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.AppViewModelProvider
import com.example.creditsapp.R
import com.example.creditsapp.presentation.components.TopBar
import com.example.creditsapp.presentation.viewmodel.ProfileViewModel
import com.example.creditsapp.ui.theme.CreditsAppTheme

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val profileUiState by viewModel.profileUiState.collectAsState()

    Scaffold(
        topBar = { TopBar(R.string.profile, navigateBack = { navController.popBackStack() }) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                profileUiState.profileData?.let { ProfilePictureAndName(it.firstName) }
                Spacer(modifier = Modifier.height(20.dp))


                Column {
                    SectionText(title = stringResource(R.string.personal_information))
                    Spacer(modifier = Modifier.height(16.dp))
                    profileUiState.profileData?.let { ProfileOption(title = stringResource(R.string.name), value = it.firstName + " " + it.lastName) }
                    profileUiState.profileData?.let {
                        ProfileOption(
                            title = stringResource(R.string.degree_name),
                            value = it.degreeName
                        )
                    }
                    profileUiState.profileData?.let { ProfileOption(title = stringResource(R.string.email), value = it.email) }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column {
                    SectionText(title = stringResource(R.string.settings))
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileOption(title = stringResource(R.string.language), value = "Español")
                    ProfileSwitchOption(title = stringResource(R.string.dark_mode), value = true)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column {
                    SectionText(title = stringResource(R.string.security))
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileOption(title = stringResource(R.string.password), value = "*****")
                }
            }
        }
    )
}


@Composable
fun SectionText(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.secondaryContainer
    )
}

@Composable
fun ProfileOption(
    title: String,
    value: String
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }

}

@Composable
fun ProfileSwitchOption(title: String, value: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title, style = MaterialTheme.typography.titleSmall)
        Switch(
            checked = value,
            onCheckedChange = {},
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