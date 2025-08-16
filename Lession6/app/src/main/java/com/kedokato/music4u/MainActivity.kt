package com.kedokato.music4u

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.compose.AppTheme
import com.kedokato.music4u.navigation.AppNavigation
import com.kedokato.music4u.presentation.config.ThemeManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = Color.Transparent.toArgb()
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars =
            true
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            false
        setContent {
                // Thiết lập màu sắc của status bar
                window.statusBarColor = Color.Transparent.toArgb()

            val themeManager = remember { ThemeManager() }

            AppTheme(darkTheme = themeManager.isDarkTheme) {
                // Cập nhật status bar theo theme
                WindowCompat.getInsetsController(
                    window,
                    window.decorView
                ).isAppearanceLightStatusBars =
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