package com.example.fr.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fr.ui.screens.FontScale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FontViewModel : ViewModel() {
    private val _fontScale = MutableStateFlow(FontScale("Medium", 1.0f))
    val fontScale = _fontScale.asStateFlow()

    fun saveFontScale(newFontScale: FontScale) {
        _fontScale.value = newFontScale
    }
}