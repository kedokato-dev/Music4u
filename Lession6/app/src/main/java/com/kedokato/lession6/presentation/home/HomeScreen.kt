package com.kedokato.lession6.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R

@Composable
fun HomeScreen(
    modifier: Modifier,
    onProfileClick: () -> Unit,
) {
    val colorScheme = getCurrentColorScheme()
    Column(
        modifier = modifier.fillMaxSize()
            .background(color = colorScheme.background),
    ) {

        ProfileTopAppBar(
            modifier = Modifier,
            onProfileClick = onProfileClick
        )
        Text(
            text = "Home Screen",
            modifier = modifier
                .then(modifier),
            color = colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppBar(modifier: Modifier,
                    onProfileClick: () -> Unit ) {
    val colorScheme = getCurrentColorScheme()
    TopAppBar(
        title = {
            Text("")
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.background,
            scrolledContainerColor = colorScheme.background,
            titleContentColor = colorScheme.background
        ),

        actions = {
            Icon(
                painter = painterResource(id = R.drawable.person),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onProfileClick() },
                tint = colorScheme.onBackground
            )
        }
    )
}