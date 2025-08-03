package com.example.creditsapp.presentation.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.launchInScope(block: suspend () -> Unit) {
    this.launch { block() }
}
