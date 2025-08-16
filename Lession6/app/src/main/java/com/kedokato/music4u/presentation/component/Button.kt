package com.kedokato.music4u.presentation.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.R


@Composable
fun Button(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = getCurrentColorScheme().primary,
            contentColor = Color.White
        )
    ) {
        Text(text)
    }
}

@Composable
fun ButtonLogout(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = getCurrentColorScheme().secondary,
            contentColor = Color.Red
        ),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logout),
            contentDescription = "Logout Icon",
            tint = Color.Red
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(text)
    }
}


@Preview
@Composable
fun ButtonPreview() {
    ButtonLogout(
        text = "Logout",
        modifier = Modifier,
        onClick = {}
    )
}