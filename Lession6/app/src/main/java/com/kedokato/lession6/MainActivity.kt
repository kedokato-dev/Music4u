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
            var typeDisplayState by rememberSaveable { mutableStateOf(true) }
            Lession6Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        PlayListTopBar(
                            typeDisplay = typeDisplayState,
                            onToggleDisplay = { typeDisplayState = !typeDisplayState })
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = innerPadding.calculateTopPadding())
                            .background(Color.White),
                    ) {
                        PlayListScreen(typeDisplayState)
                    }
                }
            }
        }
    }
}