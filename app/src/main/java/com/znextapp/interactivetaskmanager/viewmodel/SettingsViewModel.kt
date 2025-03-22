package com.znextapp.interactivetaskmanager.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.znextapp.interactivetaskmanager.data.ThemePreference
import com.znextapp.interactivetaskmanager.utils.toHex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themePreference: ThemePreference // Injected properly
) : ViewModel() {

    private val _primaryColor = MutableStateFlow("#6200EE") // Default color in HEX
    val primaryColor: StateFlow<String> = _primaryColor.asStateFlow()

    init {
        viewModelScope.launch {
            themePreference.getPrimaryColor().collect { colorHex ->
                _primaryColor.value = colorHex
            }
        }
    }

    fun updatePrimaryColor(color: Color) {
        val hexColor = color.toHex()
        _primaryColor.value = hexColor

        // Save color in DataStore
        viewModelScope.launch {
            themePreference.savePrimaryColor(hexColor)
        }
    }
}
