package com.example.creditsapp.presentation.viewmodel.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creditsapp.data.repository.ActividadesRepository
import com.example.creditsapp.domain.model.Actividad
import com.example.creditsapp.presentation.utilities.UiState
import com.example.creditsapp.presentation.utilities.fetchAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

class ActivitiesViewModel(
    private val actividadesRepository: ActividadesRepository
) : ViewModel() {

    private val filterActions = MutableSharedFlow<FilterAction>(replay = 0, extraBufferCapacity = 1)

    private val filterState: Flow<FilterState> = filterActions
        .scan(FilterState()) { currentState, action -> FilterReducer.reducer(currentState, action) }

    private val actividadesResult: Flow<Result<List<Actividad>>> = fetchAsFlow {
        actividadesRepository.getActividades()
    }

    val uiState: StateFlow<UiState<ActividadesUiData>> = combine(
        actividadesResult,
        filterState
    ) { result, filter ->
        result.fold(
            onSuccess = { actividades ->
                val filtradas = actividades.applyFilter(filter)
                UiState.Success(ActividadesUiData(filtradas, filter))
            },
            onFailure = { UiState.Error }
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Loading)


    fun onSortOptionSelected(option: SortOption) {
        filterActions.tryEmit(FilterAction.SetSortOption(option))
    }

    fun onTypeFilter(type: Int) {
        filterActions.tryEmit(FilterAction.SetType(type))
    }

    fun onCreditToggle(credit: Double) {
        filterActions.tryEmit(FilterAction.ToggleCredit(credit))
    }
}