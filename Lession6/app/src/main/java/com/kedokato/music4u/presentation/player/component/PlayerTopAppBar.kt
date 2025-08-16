package com.kedokato.music4u.presentation.player.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerTopAppBar(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit,
    onClosePlayer: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.player_screen_title), style = MaterialTheme.typography.headlineMedium) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = getCurrentColorScheme().background,
            titleContentColor = getCurrentColorScheme().onBackground
        ) ,
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = stringResource(R.string.back_to_home),
                    tint = getCurrentColorScheme().onBackground
                )
            }
        },
        actions = {
            IconButton(
                onClick = onClosePlayer,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = stringResource(R.string.close_player_screen),
                    tint = getCurrentColorScheme().onBackground
                )
            }
        },
        windowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0)
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewPlayerTopAppBar() {
    PlayerTopAppBar(
        modifier = Modifier,
        onNavigationIconClick = {}
    )
}