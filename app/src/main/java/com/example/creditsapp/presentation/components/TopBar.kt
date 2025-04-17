package com.example.creditsapp.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.creditsapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    @StringRes titleRes: Int,
    navigateBack: (() -> Unit)? = null,
    navigateToProfile: (() -> Unit)? = null
){
    TopAppBar(
        title = {
            Text(
                stringResource(titleRes),
                style = MaterialTheme.typography.labelLarge
            )
        },
        navigationIcon = {
            navigateBack?.let {
                IconButton(onClick = { navigateBack() }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        },
        actions = {
            navigateToProfile?.let {
                IconButton(onClick = { navigateToProfile() }) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    )
}