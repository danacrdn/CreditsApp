package com.example.creditsapp.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.AppViewModelProvider
import com.example.creditsapp.R
import com.example.creditsapp.domain.model.Aviso
import com.example.creditsapp.presentation.components.ErrorScreen
import com.example.creditsapp.presentation.components.LoadingScreen
import com.example.creditsapp.presentation.components.TopBar
import com.example.creditsapp.presentation.navigation.Screen
import com.example.creditsapp.presentation.utilities.UiState
import com.example.creditsapp.presentation.utilities.formatFecha
import com.example.creditsapp.presentation.viewmodel.announcements.AnnouncementsViewModel
import com.example.creditsapp.ui.theme.CreditsAppTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnnouncementsScreen(
    navController: NavController,
    viewModel: AnnouncementsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                R.string.announcements, navigateBack = { navController.popBackStack() },
                navigateToProfile = { navController.navigate(Screen.Profile.name) })
        },
        content = { paddingValues ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (val state = uiState.value) {
                    UiState.Error -> ErrorScreen()
                    UiState.Loading -> LoadingScreen()
                    is UiState.Success -> {
                        val avisos = state.data

                        if (avisos.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No hay avisos disponibles.")
                            }
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(avisos) { aviso ->
                                    AvisoCard(aviso = aviso)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AvisoCard(aviso: Aviso, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
    ) {
        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            // Título del Aviso
            Text(
                text = aviso.titulo,
                style = MaterialTheme.typography.titleSmall
                .copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Fecha del Aviso
            Text(
                text = formatFecha(aviso.fecha),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Mensaje del Aviso
            Text(
                text = aviso.mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Información de Departamento (si existe)
            aviso.departamentoNombre?.let {
                Text(
                    text = "Departamento: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Información de Coordinador (si existe)
            if (aviso.coordinadorNombre != null && aviso.coordinadorApellido != null) {
                Text(
                    text = "Coordinador: ${aviso.coordinadorNombre} ${aviso.coordinadorApellido}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else if (aviso.coordinadorNombre != null) {
                Text(
                    text = "Coordinador: ${aviso.coordinadorNombre}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewCard() {
    CreditsAppTheme {
        AvisoCard(
            aviso = Aviso(
                id = 1,
                titulo = "Hola a todos",
                mensaje = "Este es un mensaje de prueba",
                fecha = "26/07/2025",
                departamentoNombre = "Departamento de Sistemas y Computación",
                departamentoId = null,
                coordinadorId = null,
                coordinadorNombre = null,
                coordinadorApellido = null,
            )
        )

    }
}
