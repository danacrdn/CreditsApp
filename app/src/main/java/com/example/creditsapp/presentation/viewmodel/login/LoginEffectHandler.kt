package com.example.creditsapp.presentation.viewmodel.login

import com.example.creditsapp.data.repository.AuthRepository
import com.example.creditsapp.data.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginEffectHandler(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferencesRepository,
    private val scope: CoroutineScope,
    private val state: MutableStateFlow<LoginUiState>,
    private val effectFlow: MutableSharedFlow<LoginEffect>
) {

    fun handle(effect: LoginEffect) {
        when (effect) {
            is LoginEffect.ShowSnackbar -> handleShowSnackbar(effect)
            is LoginEffect.PerformLogin -> handlePerformLogin(effect)
        }
    }

    private fun handleShowSnackbar(effect: LoginEffect.ShowSnackbar) {
        launchInScope {
            effectFlow.emit(effect)
        }
    }

    private fun handlePerformLogin(effect: LoginEffect.PerformLogin) {
        launchInScope {
            val result = runCatching { authRepository.login(effect.request) }
            state.update { it.copy(isLoading = false) }

            result.onSuccess {
                userPreferences.saveUserId(it.alumnoId)
                effectFlow.emit(LoginEffect.ShowSnackbar(LoginUiMessageEvent.LoginSuccess))
                state.update { it.copy(loginSuccess = true) }
            }.onFailure {
                effectFlow.emit(LoginEffect.ShowSnackbar(LoginUiMessageEvent.LoginFailed))
            }
        }
    }

    private fun launchInScope(block: suspend () -> Unit) {
        scope.launch { block() }
    }
}
