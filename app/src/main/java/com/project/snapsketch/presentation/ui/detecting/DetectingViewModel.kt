package com.project.snapsketch.presentation.ui.detecting

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.snapsketch.R
import com.project.snapsketch.domain.usecase.GetDetectedImageUseCase
import com.project.snapsketch.domain.usecase.SaveImageUseCase
import com.project.snapsketch.presentation.shared.DefaultEvent
import com.project.snapsketch.presentation.shared.LoadingUiState
import com.project.snapsketch.presentation.utils.ImageConverter.toBitmap
import com.project.snapsketch.presentation.utils.ImageConverter.toBytes
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
class DetectingViewModel @Inject constructor(
    private val getDetectedImageUseCase: GetDetectedImageUseCase,
    private val saveImageUseCase: SaveImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoadingUiState.init())
    val uiState: StateFlow<LoadingUiState> = _uiState.asStateFlow()

    private val _getDetectedImageEvent = MutableSharedFlow<DefaultEvent>()
    val getDetectedImageEvent: SharedFlow<DefaultEvent> = _getDetectedImageEvent.asSharedFlow()

    private val _getDetectedImage = MutableStateFlow<Bitmap?>(null)
    val getDetectedImage: StateFlow<Bitmap?> = _getDetectedImage.asStateFlow()

    private val _saveImageEvent = MutableSharedFlow<DefaultEvent>()
    val saveImageEvent: SharedFlow<DefaultEvent> = _saveImageEvent.asSharedFlow()

    fun detectingImage(uri: Uri, th1: Double, th2: Double, context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            runCatching {
                val inputImage = uri.toBytes(context)

                if (inputImage != null) {
                    val detectedImage = getDetectedImageUseCase(inputImage, th1, th2)
                    val bitmap = detectedImage.toBitmap()
                    _getDetectedImage.update { bitmap }
                } else {
                    _getDetectedImageEvent.emit(DefaultEvent.Failure(R.string.detecting_msg_fail_process))
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
                _getDetectedImageEvent.emit(DefaultEvent.Failure(R.string.detecting_msg_fail_process))
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                _getDetectedImageEvent.emit(DefaultEvent.Success)
            }
        }
    }

    fun saveImage(uri: Uri, context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            runCatching {
                val inputImage = uri.toBytes(context)

                if (inputImage != null) {
                    saveImageUseCase(inputImage)
                } else {
                    _saveImageEvent.emit(DefaultEvent.Failure(R.string.detecting_msg_fail_save))
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
                _saveImageEvent.emit(DefaultEvent.Failure(R.string.detecting_msg_fail_save))
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                _saveImageEvent.emit(DefaultEvent.Success)
            }
        }
    }
}