package com.project.snapsketch.presentation.shared

import androidx.annotation.StringRes

sealed interface DefaultEvent {
    data object Success : DefaultEvent
    data class Failure(@StringRes val msg: Int) : DefaultEvent
}