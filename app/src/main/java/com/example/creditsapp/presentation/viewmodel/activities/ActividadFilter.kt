package com.example.creditsapp.presentation.viewmodel.activities

import com.example.creditsapp.domain.model.Actividad

fun List<Actividad>.applyFilter(filter: FilterState): List<Actividad> =
    this.filter { actividad ->
        (filter.selectedCredits.isEmpty() || actividad.creditos in filter.selectedCredits) &&
                (filter.selectedType == null || actividad.tipoActividad == filter.selectedType)
    }.let {
        when (filter.sortOption) {
            SortOption.NONE -> it
            SortOption.BY_NAME -> it.sortedBy { it.nombre }
            SortOption.BY_DATE -> it.sortedBy { it.fechaInicio }
        }
    }