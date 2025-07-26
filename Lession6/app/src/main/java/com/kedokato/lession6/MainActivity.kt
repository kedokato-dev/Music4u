package com.kedokato.lession6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.compose.AppTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.kedokato.lession6.ui.theme.Lession6Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars =
            false


        setContent {
            var isEditState by rememberSaveable { mutableStateOf(false) }
            val themeManager = remember { ThemeManager() }


            AppTheme(darkTheme = themeManager.isDarkTheme) {
                // Cập nhật status bar theo theme
                WindowCompat.getInsetsController(
                    window,
                    window.decorView
                )?.isAppearanceLightStatusBars =
                    !themeManager.isDarkTheme

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        ProfileTopBar(
                            title = "Profile",
                            modifier = Modifier,
                            isEdit = isEditState,
                            onIconClick = {
                                isEditState = !isEditState
                            },
                            onThemeToggle = {
                                themeManager.toggleTheme()
                            },
                            isDarkTheme = themeManager.isDarkTheme,
                        )
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Column {
                            ProfileView(
                                isEditMode = isEditState,
                                onEditModeChange = { isEditState = it },
                                isDarkTheme = themeManager.isDarkTheme,
                                modifier = Modifier
                            )
                        }

        setContent {
            var typeDisplayState by rememberSaveable { mutableStateOf(true) }
            var isSort by rememberSaveable { mutableStateOf(false) }
            Lession6Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        PlayListTopBar(
                            typeDisplay = typeDisplayState,
                            onToggleDisplay = { typeDisplayState = !typeDisplayState },
                            isSort = isSort,
                            onSort = { isSort = !isSort },
                            onCancelSort = {isSort = !isSort},
                        )


                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = innerPadding.calculateTopPadding())
                            .background(Color.White),
                    ) {
                        PlayListScreen(typeDisplayState, isSort)
                    }
                }
            }

        }
    }
}