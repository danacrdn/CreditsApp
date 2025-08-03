package com.example.creditsapp.presentation.viewmodel.activities

import com.example.creditsapp.domain.model.Actividad

data class ActividadesUiData(
    val actividades: List<Actividad>,
    val filterState: FilterState
)

enum class SortOption {
    NONE, BY_NAME, BY_DATE
}

data class FilterState(
    val sortOption: SortOption = SortOption.NONE,
    val selectedCredits: Set<Double> = emptySet(),
    val selectedType: Int? = null
)

sealed class FilterAction {
    data class SetSortOption(val option: SortOption) : FilterAction()
    data class SetType(val type: Int?) : FilterAction()
    data class ToggleCredit(val credit: Double) : FilterAction()
}