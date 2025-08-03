package com.example.creditsapp.presentation.utilities

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// funciones puras
sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    object Error : UiState<Nothing>
}

sealed class ValidationResult<out T> {
    object Success : ValidationResult<Nothing>()
    data class Error<T>(val errorType: T) : ValidationResult<T>()
}

fun <T> Boolean.toValidationResult(errorType: T): ValidationResult<T> =
    if (this) ValidationResult.Success else ValidationResult.Error(errorType)

fun <T> ValidationResult<T>.flatMap(
    transform: () -> ValidationResult<T>
): ValidationResult<T> = when (this) {
    ValidationResult.Success       -> transform()
    is ValidationResult.Error<T>   -> this
}

fun <T> List<() -> ValidationResult<T>>.chainValidations(): ValidationResult<T> =
    fold(ValidationResult.Success as ValidationResult<T>) { acc, validation ->
        acc.flatMap { validation() }
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



