package com.example.creditsapp.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.creditsapp.R
import com.example.creditsapp.domain.model.Post
import com.example.creditsapp.presentation.viewmodel.PostsUiState
import com.example.creditsapp.presentation.components.TopBar
import com.example.creditsapp.presentation.navigation.Screen

@Composable
fun PostsScreen(
    navController: NavController,
    postsUiState: PostsUiState
) {
    Scaffold(
        topBar = {
            TopBar(
                R.string.questions_and_suggestions, navigateBack = { navController.popBackStack() },
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
                when (postsUiState) {
                    PostsUiState.Error -> ErrorScreen()
                    PostsUiState.Loading -> LoadingScreen()
                    is PostsUiState.Success -> PostsListScreen(postsUiState.posts)
                }
            }
        }
    )
}

@Composable
fun PostsListScreen(
    posts: List<Post>
) {
    var favoriteIsClicked by remember { mutableStateOf(false) }

    LazyColumn {
        items(
            items = posts,
            key = { post -> post.id }
        ) { post ->
            PostCard(
                post = post,
                favoriteIsClicked = favoriteIsClicked,
                onFavoriteClicked = { favoriteIsClicked = !favoriteIsClicked },
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    favoriteIsClicked: Boolean,
    onFavoriteClicked: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                if (favoriteIsClicked) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = null,
                        modifier = Modifier.clickable { onFavoriteClicked() }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.clickable { onFavoriteClicked() }
                    )
                }

                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
fun ErrorScreen() {
    Text(stringResource(R.string.error))
    Button(onClick = {}) {
        Text(stringResource(R.string.retry))
    }
}
