package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.creditsapp.presentation.viewmodel.ActivityDetailsViewModel
import com.example.creditsapp.ui.theme.CreditsAppTheme


@Composable
fun ActivityDetailsScreen(
    activityId: Int,
    navController: NavController,
    viewModel: ActivityDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory )
) {
    val activityUiState by viewModel.activityUiState.collectAsState()

    Scaffold (
        topBar = { TopBar(R.string.activity, navigateBack = { navController.popBackStack() }) },
        content = { paddingValues ->

            Column (modifier = Modifier.padding(paddingValues)){

                Box(modifier = Modifier.fillMaxSize()) {
                    Column {
                        ActivityImage(R.drawable.activity)
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            activityUiState.activity?.let {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            activityUiState.activity?.let {
                                ActivityDetail(
                                    icon = Icons.Filled.DateRange,
                                    detailText = it.date,
                                    secondDetail = activityUiState.activity!!.hour
                                )
                            }
                            activityUiState.activity?.let {
                                ActivityDetail(
                                    icon = Icons.Filled.Place,
                                    detailText = it.place
                                )
                            }
                            ActivityDetail(
                                icon = Icons.Filled.Person,
                                detailText = activityUiState.activity?.spots.toString() + " alumnos"
                            )
                            ActivityDetail(
                                icon = Icons.Filled.Add,
                                detailText = activityUiState.activity?.value.toString() + " créditos"
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            SignUpFloatingButton()
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SignUpFloatingButton() {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxSize()
    ){
        FloatingActionButton(
            onClick = { },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, null)
        }

    }
}

@Composable
fun NotFoundActivityText() {
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)){
        Text(text = stringResource(R.string.not_found_activity),
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center)
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
){
    Row (verticalAlignment = Alignment.CenterVertically){
        Icon(
            imageVector = icon,
            contentDescription = "Search",
            modifier = Modifier.size(30.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = detailText,
                style = MaterialTheme.typography.bodyMedium,
            )
            secondDetail?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun ActivityDetailsScreenPreview(){
    CreditsAppTheme {
        //ActivityDetailsScreen()
    }
}