package com.kedokato.music4u.presentation.playlist.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayListTopBar(
    tittle: String = "My Playlist",
    typeDisplay: Boolean,
    onToggleDisplay: () -> Unit,
    isSort: Boolean,
    onSort: () -> Unit,
    onCancelSort: () -> Unit
) {
    TopAppBar(
        title = { Text(text = tittle, style = MaterialTheme.typography.headlineSmall,
            maxLines = 1, overflow = TextOverflow.Ellipsis) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = getCurrentColorScheme().background,
            titleContentColor = getCurrentColorScheme().onBackground,
            actionIconContentColor = getCurrentColorScheme().onBackground,
            navigationIconContentColor = getCurrentColorScheme().onBackground,
            scrolledContainerColor = getCurrentColorScheme().background,
        ),
        windowInsets = WindowInsets(0, 0, 0, 0),
        navigationIcon = {
            if (isSort) {
                Icon(
                    painter = painterResource(R.drawable.cancel),
                    contentDescription = "Close Sort",
                    tint = getCurrentColorScheme().onBackground,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(24.dp)
                        .clickable { onCancelSort() }
                )
            }
        },
        actions = {
            if (isSort) {
                Icon(
                    painter = painterResource(R.drawable.tick),
                    contentDescription = "Sort Icon",
                    tint = getCurrentColorScheme().onBackground,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp)
                        .clickable { onSort() }
                )
            } else {
                Icon(
                    painter = painterResource(
                        if (typeDisplay) R.drawable.grid else R.drawable.list
                    ),
                    contentDescription = "Grid/List Icon",
                    tint =getCurrentColorScheme().onBackground,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp)
                        .clickable { onToggleDisplay() }
                )
                Icon(
                    painter = painterResource(R.drawable.sort),
                    contentDescription = "Sort Icon",
                    tint = getCurrentColorScheme().onBackground,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp)
                        .clickable { onSort() }
                )
            }
        },
    )
}

@Preview
@Composable
private fun PreviewTopAppBar() {
    PlayListTopBar(
        tittle = "My Playlist",
        typeDisplay = true,
        onToggleDisplay = {},
        isSort = false,
        onSort = {},
        onCancelSort = {}
    )
    
}