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
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
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
import com.example.creditsapp.presentation.viewmodel.UserActivitiesUiState
import com.example.creditsapp.presentation.viewmodel.UserActivitiesViewModel
import com.example.creditsapp.ui.theme.CreditsAppTheme


@Composable
fun UserActivitiesScreen(
    navController: NavController,
    viewModel: UserActivitiesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                R.string.activity_history,
                navigateBack = { navController.popBackStack() })
        },
        content = { paddingValues ->

            when(val state = uiState.value){
                UserActivitiesUiState.Error -> ErrorScreen()
                UserActivitiesUiState.Loading -> LoadingScreen()
                is UserActivitiesUiState.Success -> {
                    val actividades = state.actividades
                    if (actividades.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.unavailable_activities))
                        }
                    } else {
                        Column(modifier = Modifier.padding(paddingValues)) {
                            UserActivitiesList(actividades, navController)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun UserActivitiesList(userActivitiesList: List<CursoAlumno>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(userActivitiesList) { activity ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 8.dp, end = 16.dp)
                    .wrapContentHeight()
            ) {
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
                        onClick = { navController.navigate("activity_details/${activity.actividadId}") },
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                            contentDescription = null
                        )
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun CompletedActivitiesScreenPreview() {
    CreditsAppTheme {
        //CompletedActivitiesScreen()
    }
}
