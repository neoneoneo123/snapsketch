package com.project.snapsketch.presentation.shared

data class LoadingUiState(
    val isLoading: Boolean = false
) {
    companion object {
        fun init() = LoadingUiState(
            isLoading = false
        )
    }
}
