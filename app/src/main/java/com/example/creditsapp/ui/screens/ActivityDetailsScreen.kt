package com.example.creditsapp.ui.screens

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.creditsapp.R
import com.example.creditsapp.data.activities
import com.example.creditsapp.ui.components.TopBar
import com.example.creditsapp.ui.theme.CreditsAppTheme


@Composable
fun ActivityDetailsScreen(
    activityId: Int,
    navController: NavController
) {
    Scaffold (
        topBar = { TopBar(R.string.activity, navigateBack = { navController.popBackStack() }) },
        content = { paddingValues ->

            Column (modifier = Modifier.padding(paddingValues)){
                val activity = activities.find { it.id == activityId }

                Box(modifier = Modifier.fillMaxSize()) {
                    Column {
                        if (activity != null) {

                            ActivityImage(activity.image)
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = activity.name,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                ActivityDetail(
                                    icon = Icons.Filled.DateRange,
                                    detailText = activity.date,
                                    secondDetail = activity.hour
                                )
                                ActivityDetail(
                                    icon = Icons.Filled.Place,
                                    detailText = activity.place
                                )
                                ActivityDetail(
                                    icon = Icons.Filled.Person,
                                    detailText = activity.spots
                                )
                                ActivityDetail(
                                    icon = Icons.Filled.Add,
                                    detailText = activity.creditValue.toString()
                                )
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                Text(
                                    text = stringResource(R.string.download),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            NotFoundActivityText()
                        }
                    }
                }
            }
        }
    )
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
        //ActivityDetailsScreen(activityId = 3)
    }
}