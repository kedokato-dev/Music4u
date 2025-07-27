package com.kedokato.lession6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.example.compose.AppTheme
import com.kedokato.lession6.navigation.AppNavigation
import com.kedokato.lession6.view.config.ThemeManager
import com.kedokato.lession6.view.playlist.PlayListScreen
import com.kedokato.lession6.view.playlist.PlayListTopBar
import com.kedokato.lession6.view.profile.ProfileTopBar
import com.kedokato.lession6.view.profile.ProfileView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars =
            false


        setContent {
            val themeManager = remember { ThemeManager() }

            AppTheme(darkTheme = themeManager.isDarkTheme) {
                // Cập nhật status bar theo theme
                WindowCompat.getInsetsController(
                    window,
                    window.decorView
                )?.isAppearanceLightStatusBars =
                    !themeManager.isDarkTheme

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}