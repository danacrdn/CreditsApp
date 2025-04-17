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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.LocationOn
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
    viewModel: ActivityDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
                                    icon = Icons.Rounded.CalendarMonth,
                                    detailText = it.date,
                                    secondDetail = activityUiState.activity!!.hour
                                )
                            }
                            activityUiState.activity?.let {
                                ActivityDetail(
                                    icon = Icons.Rounded.LocationOn,
                                    detailText = it.place
                                )
                            }
                            ActivityDetail(
                                icon = Icons.Rounded.Groups,
                                detailText = activityUiState.activity?.spots.toString() + " alumnos"
                            )
                            ActivityDetail(
                                icon = Icons.Rounded.Add,
                                detailText = activityUiState.activity?.value.toString() + " créditos"
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                    SignUpFloatingButton { viewModel.insertActivityUser() }
                }
            }
        }
    )
}

@Composable
fun SignUpFloatingButton(
    onClickButton: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxSize()
    ){
        FloatingActionButton(
            onClick = { onClickButton() },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Rounded.Add, null)
        }

    }
}

//@Composable
//fun NotFoundActivityText() {
//    Column (modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp)){
//        Text(text = stringResource(R.string.not_found_activity),
//            style = MaterialTheme.typography.displaySmall,
//            textAlign = TextAlign.Center)
//    }
//}

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