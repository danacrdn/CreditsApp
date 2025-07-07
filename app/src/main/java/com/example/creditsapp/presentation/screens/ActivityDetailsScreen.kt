package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.creditsapp.AppViewModelProvider
import com.example.creditsapp.R
import com.example.creditsapp.presentation.components.TopBar
import com.example.creditsapp.presentation.utilities.formatDate
import com.example.creditsapp.presentation.viewmodel.ActivityDetailsViewModel
import com.example.creditsapp.presentation.viewmodel.UiEvent
import com.example.creditsapp.ui.theme.CreditsAppTheme


@Composable
fun ActivityDetailsScreen(
    activityId: Int,
    navController: NavController,
    viewModel: ActivityDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val activityUiState by viewModel.activityUiState.collectAsState()
    val userId by viewModel.id.collectAsState()
    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val formatedDate = activityUiState.activity?.let { formatDate(it.date) }


    /*
    Launched effect for a secondary event, it defines the message that the UI will display
    when the user adds or deletes an activity. In this case, the viewmodel passes an state
    from an enum class, and based of this state, the launched effect assigns the correct message.
    */
    LaunchedEffect(message) {
        message?.let { msg ->
            snackbarHostState.showSnackbar(
                message = when (msg)  {
                    UiEvent.AddSuccess -> context.getString(R.string.added_activity)
                    UiEvent.AddError -> context.getString(R.string.add_error)
                    UiEvent.DeleteSuccess -> context.getString(R.string.deleted_activity)
                    UiEvent.DeleteError -> context.getString(R.string.delete_error)
                },
                duration = SnackbarDuration.Short
            )
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = { TopBar(R.string.activity, navigateBack = { navController.popBackStack() }) },
        content = { paddingValues ->

            Column(modifier = Modifier.padding(paddingValues)) {
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

                            activityUiState.activity?.let {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            if (activityUiState.userActivityState?.isMine == true) {
                                activityUiState.userActivityState?.let { ActivityStatusBadge(it.isCompleted) }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            activityUiState.activity?.let {
                                if (formatedDate != null) {
                                    ActivityDetail(
                                        icon = Icons.Rounded.CalendarMonth,
                                        detailText = formatedDate,
                                        secondDetail = activityUiState.activity!!.hour
                                    )
                                }
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
                    /* If the user doesn't have the activity in their activities, then the button
                    * will change. It will have a different option in each case.
                    * Sign Up or Delete. */
                    if (activityUiState.userActivityState?.isMine == false) {
                        SignUpFloatingButton { viewModel.insertActivityUser() }
                    } else {
                        DeleteActivityButton(onClick = {
                            viewModel.deleteActivityUser()
                        })
                    }

                    SnackbarHost(hostState = snackbarHostState)
                }
            }
        }
    )
}

@Composable
fun DeleteActivityButton(onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = { onClick() },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Rounded.Delete, null)
        }

    }
}

@Composable
fun ActivityStatusBadge(status: Boolean) {
    val bgColor = when (status) {
        true -> Color(0xFF28A745)
        false -> Color(0xFFFFC107)
    }

    Box(
        modifier = Modifier
            .background(bgColor, shape = RoundedCornerShape(15.dp))
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
            if (status) {
                Text(
                    text = "Completado",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Pendiente",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}

@Composable
fun SignUpFloatingButton(
    onClickButton: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxSize()
    ) {
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