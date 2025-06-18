package com.example.worldlist.data

/**
 * Generic class that contains data and status about loading data
 */
sealed class UiState<out T : Any> {
    data object Loading : UiState<Nothing>()
    data class Success<out T : Any>(val data: T) : UiState<T>()
    data class Error(val exception: Throwable) : UiState<Nothing>()
}
