package com.example.creditsapp.presentation.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.AppViewModelProvider
import com.example.creditsapp.R
import com.example.creditsapp.presentation.components.TopBar
import com.example.creditsapp.presentation.navigation.Screen
import com.example.creditsapp.presentation.viewmodel.HomeViewModel
import com.example.creditsapp.ui.theme.CreditsAppTheme

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                R.string.home,
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
                StudentInfo(homeUiState.userCredits.name)
                Spacer(modifier = Modifier.height(20.dp))
                CreditsCard(homeUiState.userCredits.totalCredits.toString())
                Spacer(modifier = Modifier.height(20.dp))
                OptionsGrid(navController)
                Spacer(modifier = Modifier.height(8.dp))
                Suggestions(navController)
            }
        }
    )
}

@Composable
fun Suggestions(
    navController: NavController
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        modifier = Modifier
            .width(325.dp)
            .height(100.dp)
            .clickable { navController.navigate(Screen.Posts.name) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.forum_40dp),
                contentDescription = null,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceDim,
                        shape = RoundedCornerShape(15.dp)
                    ).padding(8.dp)
            )
            Text(
                text = stringResource(R.string.questions_and_suggestions),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun StudentInfo(
    studentName: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.size(60.dp)) {
            Image(
                painter = painterResource(R.drawable.default_icon),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {}
            )
        }
        Text(
            text = "Hola, $studentName",
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

@Composable
fun CreditsCard(
    totalCredits: String
) {
    Card(
        modifier = Modifier
            .width(325.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.total_credits),
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "$totalCredits/5",
                style = MaterialTheme.typography.displayLarge
            )
        }
    }
}

@Composable
fun OptionsGrid(
    navController: NavController
) {
    Column {
        Row {
            OptionCard(
                painter = painterResource(R.drawable.event_available_40dp),
                optionText = R.string.avaliable_activities,
                onClick = { navController.navigate(Screen.Activities.name) })
            OptionCard(
                painter = painterResource(R.drawable.history_40dp),
                R.string.activity_history,
                onClick = { navController.navigate(Screen.CompletedActivities.name) })
        }
        Row {
            OptionCard(
                painter = painterResource(R.drawable.check_circle_40dp),
                R.string.my_credits,
                onClick = { navController.navigate(Screen.TotalCredits.name) })
            OptionCard(
                painter = painterResource(R.drawable.description_40dp),
                R.string.download_documents,
                onClick = { navController.navigate(Screen.Downloads.name) })
        }
    }
}

@Composable
fun OptionCard(
    painter: Painter,
    @StringRes optionText: Int,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        modifier = Modifier
            .size(170.dp)
            .padding(8.dp)
            .clickable { onClick() }

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painter,
                contentDescription = null,
                Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceDim,
                        shape = RoundedCornerShape(15.dp)
                    ).padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(optionText),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    CreditsAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            //HomeScreen(modifier = Modifier.fillMaxSize())
        }
    }
}