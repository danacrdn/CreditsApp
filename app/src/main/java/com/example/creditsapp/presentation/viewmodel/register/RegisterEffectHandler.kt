package com.example.creditsapp.presentation.viewmodel.register

import com.example.creditsapp.data.repository.AuthRepository
import com.example.creditsapp.data.repository.CarreraRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterEffectHandler(
    private val authRepository: AuthRepository,
    private val carreraRepository: CarreraRepository,
    private val scope: CoroutineScope,
    private val state: MutableStateFlow<RegisterUiState>,
    private val effectFlow: MutableSharedFlow<RegisterEffect>,
    private val sendIntent: (RegisterIntent) -> Unit
) {

    fun handle(effect: RegisterEffect) {
        when (effect) {
            is RegisterEffect.PerformRegister -> handlePerformRegister(effect)
            is RegisterEffect.ShowSnackbar -> handleShowSnackbar(effect)
            RegisterEffect.LoadCareers -> handleLoadCareers()
        }
    }

    private fun handleLoadCareers() {
        launchInScope {
            runCatching { carreraRepository.getCarreras() }
                .onSuccess { sendIntent(RegisterIntent.CareersFetched(it)) }
                .onFailure { sendIntent(RegisterIntent.CareersFailed(it.message)) }
        }

    }

    private fun handlePerformRegister(effect: RegisterEffect.PerformRegister) {
        launchInScope {
            val result = runCatching { authRepository.register(effect.request) }
            state.update { it.copy(isRegistering = false) }

            result.onSuccess {
                effectFlow.emit(RegisterEffect.ShowSnackbar(RegisterUiMessageEvent.RegistrationSuccess))
                state.update { it.copy(registrationSuccess = true) }
            }.onFailure {
                effectFlow.emit(RegisterEffect.ShowSnackbar(RegisterUiMessageEvent.RegistrationFailed))
            }
        }
    }

    private fun handleShowSnackbar(effect: RegisterEffect.ShowSnackbar) {
        launchInScope {
            effectFlow.emit(effect)
        }
    }

    private fun launchInScope(block: suspend () -> Unit) {
        scope.launch { block() }
    }
}