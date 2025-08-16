package com.kedokato.music4u.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.R
import com.kedokato.music4u.presentation.component.Logo
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(modifier: Modifier,
                 onNavigationLogin: () -> Unit = {}) {
    val colorScheme = getCurrentColorScheme()
    Column(
        modifier = modifier.background(colorScheme.background)
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        LaunchedEffect(Unit) {
            delay(2000)
            onNavigationLogin()
        }


        Logo(
            modifier = Modifier.size(300.dp),
        )

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.background),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(
        modifier = Modifier,
    )
}