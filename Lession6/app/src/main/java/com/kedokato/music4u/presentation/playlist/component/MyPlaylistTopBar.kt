package com.kedokato.music4u.presentation.playlist.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPlaylistTopBar(
    onAddClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = "My Playlist", style = MaterialTheme.typography.headlineMedium) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = getCurrentColorScheme().background,
            titleContentColor = getCurrentColorScheme().onBackground,
            actionIconContentColor = getCurrentColorScheme().onBackground,
            navigationIconContentColor = getCurrentColorScheme().onBackground,
            scrolledContainerColor = getCurrentColorScheme().background,
        ),
        windowInsets = WindowInsets(0, 0, 0, 0),
        actions = {
            Icon(
                painter = painterResource(R.drawable.add),
                contentDescription = "Add Icon",
                tint = getCurrentColorScheme().onBackground,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(24.dp)
                    .clickable { onAddClick() }
            )
        }

    )
}