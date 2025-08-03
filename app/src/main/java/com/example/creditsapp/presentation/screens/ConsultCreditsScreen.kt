package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.AppViewModelProvider
import com.example.creditsapp.R
import com.example.creditsapp.domain.model.CursoAlumno
import com.example.creditsapp.presentation.components.ErrorScreen
import com.example.creditsapp.presentation.components.LoadingScreen
import com.example.creditsapp.presentation.components.TopBar
import com.example.creditsapp.presentation.navigation.Screen
import com.example.creditsapp.presentation.utilities.UiState
import com.example.creditsapp.presentation.viewmodel.historial.ActivitiesHistorialViewModel
import com.example.creditsapp.ui.theme.CreditsAppTheme

@Composable
fun ConsultCreditsScreen(
    navController: NavController,
    viewModel: ActivitiesHistorialViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                R.string.my_credits, navigateBack = { navController.popBackStack() },
                navigateToProfile = { navController.navigate(Screen.Profile.name) })
        },
        content = { paddingValues ->

            when (val state = uiState.value) {
                UiState.Error -> ErrorScreen()
                UiState.Loading -> LoadingScreen()
                is UiState.Success -> {
                    val actividades = state.data

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        CreditsImage()
                        Spacer(modifier = Modifier.height(20.dp))
                        CreditsListTitle()
                        Spacer(modifier = Modifier.height(20.dp))
                        CreditsList(actividades)
                    }
                }
            }
        }
    )
}

@Composable
fun CreditsList(activitiesForUser: List<CursoAlumno>) {
    Column {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
            modifier = Modifier.width(325.dp),
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Actividad",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Créditos",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )

            }

            if (activitiesForUser.isEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(18.dp),
                ) {
                    Text(text = "Aún no tienes actividades acreditadas.",  textAlign = TextAlign.Center,)
                }
            } else {
                LazyColumn {
                    items(activitiesForUser) { activity ->
                        Row(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = activity.nombre,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = activity.creditos.toString(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreditsListTitle() {
    Text(
        text = stringResource(R.string.credits_list),
        style = MaterialTheme.typography.displaySmall
    )
}

@Composable
fun CreditsImage() {
    Image(
        painter = painterResource(R.drawable.checklist),
        contentDescription = null,
        modifier = Modifier.size(350.dp)
    )
}

@Preview
@Composable
fun ConsultCreditsScreenPreview() {
    CreditsAppTheme {
        //ConsultCreditsScreen()
    }
}