package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.AppViewModelProvider
import com.example.creditsapp.R
import com.example.creditsapp.presentation.components.ErrorScreen
import com.example.creditsapp.presentation.components.LoadingScreen
import com.example.creditsapp.presentation.components.TopBar
import com.example.creditsapp.presentation.utilities.UiState
import com.example.creditsapp.presentation.viewmodel.historial.ActivitiesHistorialViewModel

@Composable
fun DownloadDocumentsScreen(
    navController: NavController,
    viewModel: ActivitiesHistorialViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                R.string.download_documents,
                navigateBack = { navController.popBackStack() })
        },
        content = { paddingValues ->

            when (val state = uiState.value) {
                UiState.Error -> ErrorScreen()
                UiState.Loading -> LoadingScreen()
                is UiState.Success -> {
                    val actividadesCompletadas = state.data

                    if (actividadesCompletadas.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.no_documents))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                        ) {

                            // Solo se muestran las actividades que ya están completadas para descargar su constancia
                            items(actividadesCompletadas) { activity ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, bottom = 8.dp, end = 16.dp)
                                        .wrapContentHeight()
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = activity.nombre,
                                                style = MaterialTheme.typography.labelLarge,
                                                maxLines = 3,
                                                modifier = Modifier.weight(1f)
                                            )
                                            IconButton(
                                                onClick = { },
                                                modifier = Modifier.size(50.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Download,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}