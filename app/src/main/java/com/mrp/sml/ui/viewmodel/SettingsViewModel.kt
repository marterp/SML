package com.mrp.sml.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrp.sml.core.constants.TransferConstants
import com.mrp.sml.data.local.preferences.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val deviceName: String = "",
    val saveHistory: Boolean = true,
    val darkMode: Boolean = false,
    val saveLocation: String = "Downloads/SML",
    val chunkSize: Int = 16_777_216,
    val networkFallback: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsManager.saveHistory.collect { value ->
                _uiState.update { it.copy(saveHistory = value) }
            }
        }
        viewModelScope.launch {
            settingsManager.deviceName.collect { value ->
                _uiState.update { it.copy(deviceName = value) }
            }
        }
        viewModelScope.launch {
            settingsManager.darkMode.collect { value ->
                _uiState.update { it.copy(darkMode = value) }
            }
        }
        viewModelScope.launch {
            settingsManager.chunkSize.collect { value ->
                _uiState.update { it.copy(chunkSize = value) }
            }
        }
    }

    fun setSaveHistory(enabled: Boolean) {
        viewModelScope.launch { settingsManager.setSaveHistory(enabled) }
    }

    fun setDeviceName(name: String) {
        viewModelScope.launch {
            settingsManager.setDeviceName(name)
            _uiState.update { it.copy(deviceName = name) }
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setDarkMode(enabled)
            _uiState.update { it.copy(darkMode = enabled) }
        }
    }

    fun setSaveLocation(uri: Uri) {
        viewModelScope.launch {
            val location = uri.toString()
            _uiState.update { it.copy(saveLocation = location) }
        }
    }

    fun setChunkSize(size: Int) {
        viewModelScope.launch {
            settingsManager.setChunkSize(size)
            _uiState.update { it.copy(chunkSize = size) }
        }
    }

    fun setNetworkFallback(enabled: Boolean) {
        _uiState.update { it.copy(networkFallback = enabled) }
    }
}
