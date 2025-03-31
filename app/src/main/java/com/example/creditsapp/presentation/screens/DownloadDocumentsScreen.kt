package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.R
import com.example.creditsapp.presentation.components.TopBar
import com.example.creditsapp.presentation.viewmodel.DownloadDocumentsViewModel

@Composable
fun DownloadDocumentsScreen(navController: NavController, viewModel: DownloadDocumentsViewModel = viewModel() ){
    val completedActivities by viewModel.completedActivities

    LaunchedEffect(Unit) {
        viewModel.filterCompletedActivities()
    }

    Scaffold (
        topBar = { TopBar(R.string.download_documents, navigateBack = { navController.popBackStack()}) },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
            ) {

                // Solo se muestran las actividades que ya están completadas para descargar su constancia
                items(completedActivities) { activity ->
                    Card(
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
                                    text = activity.name,
                                    style = MaterialTheme.typography.labelLarge,
                                    maxLines = 3,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {  }, modifier = Modifier.size(50.dp)) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}