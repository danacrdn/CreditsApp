package com.example.creditsapp.presentation.utilities

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    object Error : UiState<Nothing>
}

sealed class ValidationResult<out T> {
    object Success : ValidationResult<Nothing>()
    data class Error<T>(val errorType: T) : ValidationResult<T>()
}

fun <T, R> Result<T>.toUiState(
    mapSuccess: (T) -> R
): UiState<R> =
    this.map(mapSuccess)
        .fold(
            onSuccess = { UiState.Success(it) },
            onFailure = { UiState.Error }
        )

fun <T> Result<T>.toUiState(): UiState<T> = fold(
    onSuccess = { UiState.Success(it) },
    onFailure = { UiState.Error }
)

fun <T> fetchAsFlow(block: suspend () -> T): Flow<Result<T>> = flow {
    emit(runCatching { block() })
}



