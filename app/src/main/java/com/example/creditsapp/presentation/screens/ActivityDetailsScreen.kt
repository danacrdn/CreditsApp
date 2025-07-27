package com.example.creditsapp.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Class
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.AppViewModelProvider
import com.example.creditsapp.R
import com.example.creditsapp.presentation.components.ErrorScreen
import com.example.creditsapp.presentation.components.LoadingScreen
import com.example.creditsapp.presentation.components.TopBar
import com.example.creditsapp.presentation.utilities.formatFecha
import com.example.creditsapp.presentation.viewmodel.ActividadUiState
import com.example.creditsapp.presentation.viewmodel.ActivityDetailsViewModel
import com.example.creditsapp.presentation.viewmodel.AlumnoActividadState
import com.example.creditsapp.presentation.viewmodel.AlumnoActividadUiMessageEvent
import com.example.creditsapp.ui.theme.CreditsAppTheme


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityDetailsScreen(
    activityId: Int,
    navController: NavController,
    viewModel: ActivityDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val deleteFailedMsg = stringResource(R.string.delete_act_failed)
    val deleteSuccessMsg = stringResource(R.string.delete_act_success)
    val inscriptionFailedMsg = stringResource(R.string.add_act_failed)
    val inscriptionSuccess = stringResource(R.string.add_act_success)

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { event ->
            val text = when (event) {
                AlumnoActividadUiMessageEvent.DeleteFailed -> deleteFailedMsg
                AlumnoActividadUiMessageEvent.DeleteSuccess -> deleteSuccessMsg
                AlumnoActividadUiMessageEvent.InscriptionFailed -> inscriptionFailedMsg
                AlumnoActividadUiMessageEvent.InscriptionSuccess -> inscriptionSuccess
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
        topBar = { TopBar(R.string.activity, navigateBack = { navController.popBackStack() }) },
        floatingActionButton = {
            when (val state = uiState.value) {
                is ActividadUiState.Success -> {
                    val actState = state.alumnoActividadState
                    if (actState == AlumnoActividadState.NoInscrito) {
                        FloatingButton (Icons.Rounded.Add) { viewModel.inscribirAlumno() }
                    } else {
                        FloatingButton(Icons.Rounded.Delete) { viewModel.eliminarActividad() }
                    }
                }
                else -> {}
            }
        },
        content = { paddingValues ->

            Column(modifier = Modifier.padding(paddingValues)) {

                when (val state = uiState.value) {
                    ActividadUiState.Error -> ErrorScreen()
                    ActividadUiState.Loading -> LoadingScreen()
                    is ActividadUiState.Success -> {
                        val actividad = state.actividad

                        Box(modifier = Modifier.fillMaxSize()) {
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                ActivityImage(R.drawable.activity)
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxSize(),
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    val actState = state.alumnoActividadState

                                    val (badgeText, badgeColor) = when (actState) {
                                        AlumnoActividadState.Inscrito -> "Inscrito" to Color(
                                            0xFF4CAF50
                                        ) // verde
                                        AlumnoActividadState.EnCurso -> "En curso" to Color(
                                            0xFF2196F3
                                        )// azul
                                        AlumnoActividadState.Completado -> "Completado" to Color(
                                            0xFF9C27B0
                                        ) // morado
                                        AlumnoActividadState.Acreditado -> "Acreditado" to Color(
                                            0xFF4CAF50
                                        ) // verde
                                        AlumnoActividadState.NoAcreditado -> "No acreditado" to Color(
                                            0xFFF44336
                                        ) // rojo
                                        AlumnoActividadState.NoInscrito -> "No inscrito" to Color.Gray
                                        is AlumnoActividadState.Desconocido -> "Desconocido" to Color.DarkGray
                                    }

                                    if (actividad != null) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                            Text(
                                                text = actividad.nombre,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            ActividadStatusBadge(
                                                text = badgeText,
                                                color = badgeColor
                                            )
                                        }
                                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                        ActivityDetail(
                                            icon = Icons.Rounded.Description,
                                            detailText = actividad.descripcion,
                                        )
                                        ActivityDetail(
                                            icon = Icons.Rounded.Event,
                                            detailText = formatFecha(actividad.fechaInicio) + " a " + formatFecha(
                                                actividad.fechaFin
                                            ),
                                            secondDetail = actividad.horaInicio + " - " + actividad.horaFin
                                        )
                                        ActivityDetail(
                                            icon = Icons.Rounded.Star,
                                            detailText = actividad.creditos.toString() + " crédito/s",
                                        )
                                        ActivityDetail(
                                            icon = Icons.Rounded.Groups,
                                            detailText = actividad.capacidad.toString() + " alumnos",
                                        )
                                        ActivityDetail(
                                            icon = Icons.Rounded.Class,
                                            detailText = when (actividad.tipoActividad) {
                                                1 -> "Deportiva"
                                                2 -> "Cultural"
                                                3 -> "Tutoría"
                                                4 -> "Curso MOOC"
                                                else -> "Actividad sin categoría"
                                            },
                                        )
                                        DetailList(actividad.carreraNombres, "Carreras")

                                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
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

@Composable
fun DetailList(
    carreraNombres: List<String>,
    title: String = "Carreras"
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = MaterialTheme.colorScheme.primaryContainer
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                carreraNombres.forEach { nombre ->
                    Text(
                        text = "• $nombre",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ActividadStatusBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(15.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun FloatingButton(
    icon: ImageVector,
    onClickButton: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClickButton,
    ) {
        Icon(icon, contentDescription = null)
    }
}


@Composable
fun ActivityImage(image: Int) {
    Image(
        painter = painterResource(image),
        contentDescription = null,
    )
}

@Composable
fun ActivityDetail(
    icon: ImageVector,
    detailText: String,
    secondDetail: String? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = "Search",
            modifier = Modifier.size(30.dp),
            tint = MaterialTheme.colorScheme.primaryContainer
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = detailText,
                style = MaterialTheme.typography.labelLarge,
            )
            secondDetail?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview
@Composable
fun ActivityDetailsScreenPreview() {
    CreditsAppTheme {
        //ActivityDetailsScreen()
    }
}