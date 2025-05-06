package com.example.creditsapp.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.AppViewModelProvider
import com.example.creditsapp.R
import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.presentation.components.TopBar
import com.example.creditsapp.presentation.navigation.Screen
import com.example.creditsapp.presentation.viewmodel.ActivitiesViewModel
import com.example.creditsapp.presentation.viewmodel.SortOption

@Composable
fun ActivitiesScreen(
    navController: NavController,
    viewModel: ActivitiesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val activities by viewModel.activities.collectAsState()
    val selectedCredits by viewModel.selectedCredits.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()

    var showCreditsOptions by remember { mutableStateOf(false) }
    var isSortOptionExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                R.string.activities, navigateBack = { navController.popBackStack() },
                navigateToProfile = { navController.navigate(Screen.Profile.name) })
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Box(Modifier.fillMaxWidth()) {
                    FilterCreditsBar(
                        selectedCredits = selectedCredits,
                        onToggleCreditValue = { credit -> viewModel.toggleCreditFilter(credit) },
                        isCreditsExpanded = showCreditsOptions,
                        onCreditsExpandToggle = { showCreditsOptions = !showCreditsOptions },
                        modifier = Modifier.align(Alignment.TopStart)
                    )

                    Spacer(Modifier.width(8.dp))

                    SortByBar(
                        isSortOptionExpanded = isSortOptionExpanded,
                        onSortExpandToggle = { isSortOptionExpanded = !isSortOptionExpanded },
                        selectedSortOption = sortOption,
                        onSelectSortOption = { viewModel.setSortOption(it) },
                        modifier = Modifier.align(Alignment.TopEnd)
                    )

                }
                LazyColumn {
                    items(activities.activitiesList) { activity ->
                        ActivityItem(navController, activity)
                    }
                }
            }
        }
    )
}

@Composable
fun SortByBar(
    isSortOptionExpanded: Boolean,
    onSortExpandToggle: () -> Unit,
    selectedSortOption: SortOption,
    onSelectSortOption: (SortOption) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessVeryLow
            )
        )
    ) {
        FilterChip(
            selected = isSortOptionExpanded,
            onClick = onSortExpandToggle,
            label = { Text(stringResource(R.string.order_by)) },
        )

        DropdownMenu(
            expanded = isSortOptionExpanded,
            onDismissRequest = onSortExpandToggle,
            modifier = Modifier.padding(16.dp)
        ) {
            SortOption.entries.filter { it != SortOption.NONE }.forEach { option ->
                FilterChip(
                    selected = selectedSortOption == option,
                    onClick = {
                        if (selectedSortOption == option) {
                            onSelectSortOption(SortOption.NONE)
                        } else {
                            onSelectSortOption(option)
                        }
                    },
                    label = ({
                        Text(
                            when (option) {
                                SortOption.NONE -> stringResource(R.string.none)
                                SortOption.BY_DATE -> stringResource(R.string.by_date)
                                SortOption.BY_NAME -> stringResource(R.string.by_name)
                            }
                        )
                    })
                )
            }
        }
    }
}


@Composable
fun FilterCreditsBar(
    selectedCredits: Set<Double>,
    onToggleCreditValue: (Double) -> Unit,
    isCreditsExpanded: Boolean,
    onCreditsExpandToggle: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessVeryLow
            )
        )
    ) {
        FilterChip(
            selected = isCreditsExpanded,
            onClick = onCreditsExpandToggle,
            label = { Text(stringResource(R.string.credits)) }
        )
        DropdownMenu(
            expanded = isCreditsExpanded,
            onDismissRequest = onCreditsExpandToggle,
            modifier = Modifier.padding(16.dp)

        ) {
            listOf(0.5, 1.0, 1.5, 2.0).forEach { credit ->
                val isSelected = selectedCredits.contains(credit)
                FilterChip(
                    selected = isSelected,
                    onClick = { onToggleCreditValue(credit) },
                    label = ({ Text(credit.toString()) })
                )
            }

        }

    }

}


@Composable
fun ActivityItem(
    navController: NavController,
    activity: Activity
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, top = 8.dp)
            .height(180.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = Icons.Rounded.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceDim,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .size(60.dp)
                        .padding(8.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = activity.name,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = activity.date,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
            }
            IconButton(
                onClick = { navController.navigate("activity_details/${activity.id}") },
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = null,
                )
            }
        }
    }
}
