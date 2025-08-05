package com.kedokato.lession6.presentation.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ThemeManager {
    var isDarkTheme by mutableStateOf(true)
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }
}