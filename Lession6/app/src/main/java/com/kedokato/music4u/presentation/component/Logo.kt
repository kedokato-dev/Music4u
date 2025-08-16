package com.kedokato.music4u.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.R

@Composable
fun Logo(modifier: Modifier) {
    val colorScheme = getCurrentColorScheme()
    Image(
        painter = painterResource(id = R.drawable.logo_app),
        contentDescription = "Logo",
        modifier = modifier.size(128.dp)
            .background(colorScheme.background)
            .then(modifier)
    )
}


@Preview(showBackground = true)
@Composable
fun LogoPreview() {
    Logo(modifier = Modifier)
}