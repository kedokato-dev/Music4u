package com.kedokato.lession6.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R

@Composable
fun DisconnectInternet(
    modifier: Modifier,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(color = getCurrentColorScheme().background)
            .padding(16.dp)
        .fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.wifi_off),
            contentDescription = "No Internet Connection",
            tint = getCurrentColorScheme().onBackground,
            modifier = androidx.compose.ui.Modifier
                .size(200.dp)
                .padding(16.dp)
        )

        Text(
           text = "No internet connection, please check your connection again",
            color = getCurrentColorScheme().onBackground,
            modifier = modifier.padding(16.dp),
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Button(
            text = "Retry",
            modifier = Modifier.width(100.dp),
            onClick = {
                onRetry()
            }
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDisconnectInternet() {
    DisconnectInternet(modifier = Modifier)
}