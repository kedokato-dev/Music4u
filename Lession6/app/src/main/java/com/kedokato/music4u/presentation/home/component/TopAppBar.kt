package com.kedokato.music4u.presentation.home.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(modifier: Modifier = Modifier, title: Int, onNavigationIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(title), style = MaterialTheme.typography.titleLarge) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = getCurrentColorScheme().background,
            titleContentColor = getCurrentColorScheme().onBackground
        ) ,
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick,
                modifier = modifier.size(32.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = stringResource(R.string.back_to_home),
                    tint = getCurrentColorScheme().onBackground
                )
            }
        },
        windowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0)
    )
}