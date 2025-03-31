package com.example.creditsapp.presentation.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

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
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
        },
        actions = {
            navigateToProfile?.let {
                IconButton(onClick = { navigateToProfile() }) {
                    Icon(imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondaryContainer)
                }
            }
        }
    )
}