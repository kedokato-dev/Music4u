package com.kedokato.lession6.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R

@Composable
fun TextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: Int,
    modifier: Modifier = Modifier,
    error: String = "",
) {
    val colorScheme = getCurrentColorScheme()
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = label, color = colorScheme.onBackground) },
        leadingIcon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = colorScheme.onBackground,
                modifier = Modifier.size(18.dp)
            )
        },
        singleLine = true,
        maxLines = 1,

        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorScheme.surfaceVariant,
            unfocusedContainerColor = colorScheme.surfaceVariant,
            errorContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = colorScheme.onBackground,
            unfocusedTextColor = colorScheme.onBackground,
        ),
        modifier = modifier.fillMaxWidth(0.9f)
            .border(
            width = 1.dp,
            color = colorScheme.outline,
            shape = RoundedCornerShape(8.dp)
        ),
    )
}


@Composable
fun TextInputFieldPassword(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: Int,
    modifier: Modifier = Modifier
) {
    val colorScheme = getCurrentColorScheme()
    var hidePasswordState by remember { mutableStateOf(true) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = label, color = colorScheme.onBackground) },
        leadingIcon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = colorScheme.onBackground,
                modifier = Modifier.size(18.dp)
            )
        },
        trailingIcon = {
            val visibilityIcon = if (hidePasswordState)
                R.drawable.visibility_off else R.drawable.visibility
            Icon(
                painter = painterResource(visibilityIcon),
                contentDescription = null,
                tint = colorScheme.onBackground,
                modifier = Modifier
                    .size(18.dp)
                    .clickable { hidePasswordState = !hidePasswordState }
            )
        },
        visualTransformation = if (hidePasswordState) PasswordVisualTransformation()
        else VisualTransformation.None,
        singleLine = true,
        maxLines = 1,
        isError = false,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorScheme.surfaceVariant,
            unfocusedContainerColor = colorScheme.surfaceVariant,
            errorContainerColor = colorScheme.error,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = colorScheme.onBackground,
            unfocusedTextColor = colorScheme.onBackground,
        ),
        modifier = modifier.fillMaxWidth(0.9f)
            .border(
                width = 1.dp,
                color = colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            ))
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppTheme {
        TextInputFieldPassword(
            value = "password",
            onValueChange = {},
            label = "Password",
            icon = R.drawable.lock,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
    }
}