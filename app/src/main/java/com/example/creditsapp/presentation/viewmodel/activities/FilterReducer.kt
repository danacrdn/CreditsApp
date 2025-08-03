package com.example.creditsapp.presentation.viewmodel.activities

object FilterReducer {
    // función pura
    fun reducer(state: FilterState, action: FilterAction): FilterState = when (action) {
        is FilterAction.SetSortOption -> state.copy(sortOption = action.option)
        is FilterAction.SetType -> state.copy(
            selectedType = if (state.selectedType == action.type) null else action.type
        )

        is FilterAction.ToggleCredit -> {
            val updatedCredits = if (action.credit in state.selectedCredits) {
                state.selectedCredits - action.credit
            } else {
                state.selectedCredits + action.credit
            }
            state.copy(selectedCredits = updatedCredits)
        }
    }
}