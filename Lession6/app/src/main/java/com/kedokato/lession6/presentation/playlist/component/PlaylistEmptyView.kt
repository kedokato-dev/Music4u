package com.kedokato.lession6.presentation.playlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R

@Composable
fun PlaylistEmptyView(
    text: String = "You don't have any playlists. Click the “+” button to add",
    onClick: () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .background(getCurrentColorScheme().background)
            .fillMaxSize()

    ) {
        Text(
            text = text,
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .fillMaxWidth(),
            color = getCurrentColorScheme().onBackground,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Icon(
            painter = painterResource(id = R.drawable.big_add),
            contentDescription = "No Data",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .clickable(onClick = onClick),
            tint = getCurrentColorScheme().onBackground,
        )
    }
}

