package com.example.creditsapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.creditsapp.CreditsApp
import com.example.creditsapp.data.repository.PostsRepository
import com.example.creditsapp.domain.model.Post
import kotlinx.coroutines.launch
import java.io.IOException

// Esta clase tiene los estados ya predeterminados en caso de cargar exitosa o error
sealed interface PostsUiState {
    data class Success(val posts: List<Post>) : PostsUiState
    object Error : PostsUiState
    object Loading : PostsUiState
}

class PostsViewModel (private val postsRepository: PostsRepository) : ViewModel() {
    var postsUiState: PostsUiState by mutableStateOf(PostsUiState.Loading)
        private set

    init {
        getPosts()
    }

    private fun getPosts(){
        viewModelScope.launch {
            postsUiState = try {
                PostsUiState.Success(postsRepository.getPosts())
            } catch (e: IOException) {
                PostsUiState.Error
            }

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CreditsApp)
                val postsRepository = application.container.postsRepository
                PostsViewModel(postsRepository = postsRepository)
            }
        }
    }
}