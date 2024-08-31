package com.project.snapsketch.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.snapsketch.R
import com.project.snapsketch.domain.entity.ImageEntity
import com.project.snapsketch.domain.usecase.DeleteImageUseCase
import com.project.snapsketch.domain.usecase.GetImagesUseCase
import com.project.snapsketch.presentation.shared.DefaultEvent
import com.project.snapsketch.presentation.shared.LoadingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
    private val deleteImageUseCase: DeleteImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoadingUiState.init())
    val uiState: StateFlow<LoadingUiState> = _uiState.asStateFlow()

    private val _getImagesEvent = MutableSharedFlow<DefaultEvent>()
    val getImagesEvent: SharedFlow<DefaultEvent> = _getImagesEvent.asSharedFlow()

    private val _getImages = MutableStateFlow<List<ImageEntity>?>(emptyList())
    val getImages: StateFlow<List<ImageEntity>?> = _getImages.asStateFlow()

    private val _deleteImageEvent = MutableSharedFlow<DefaultEvent>()
    val deleteImageEvent: SharedFlow<DefaultEvent> = _deleteImageEvent.asSharedFlow()

    init {
        getImagesData()
    }

    private fun getImagesData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            runCatching {
                val images = getImagesUseCase.invoke()
                if (images != null) {
                    _getImages.update { images }
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
                _getImagesEvent.emit(DefaultEvent.Failure(R.string.home_msg_fail_get_images))
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                _getImagesEvent.emit(DefaultEvent.Success)
            }
        }
    }

    fun deleteImage(imageEntity: ImageEntity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            runCatching {
                deleteImageUseCase(imageEntity.uriString.toString())
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
                _deleteImageEvent.emit(DefaultEvent.Failure(R.string.home_msg_fail_get_images))
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                _deleteImageEvent.emit(DefaultEvent.Success)
            }
        }
    }
}