package com.kedokato.lession6.view.login

import android.widget.MediaController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.component.Button
import com.kedokato.lession6.component.Logo
import com.kedokato.lession6.component.TextInputField
import com.kedokato.lession6.component.TextInputFieldPassword

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    initialUsername: String = "",
    initialPassword: String = "",
    onSignUpClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val colorScheme = getCurrentColorScheme()
    var isChecked by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf(initialUsername) }
    var password by remember { mutableStateOf(initialPassword) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        // Nội dung chính có thể cuộn
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Logo(modifier = Modifier.size(200.dp))

            Text(
                text = stringResource(R.string.login_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                style = MaterialTheme.typography.headlineMedium,
                color = colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextInputField(
                value = username,
                onValueChange = {
                    username = it
                },
                label = stringResource(R.string.username),
                icon = R.drawable.person,
                modifier = modifier.fillMaxWidth(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextInputFieldPassword(
                value = password,
                onValueChange = {
                    password = it
                },
                label = stringResource(R.string.password),
                icon = R.drawable.lock,
                modifier = modifier.fillMaxWidth(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    modifier = Modifier.size(24.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorScheme.primary,
                        uncheckedColor = colorScheme.primary
                    ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.remember_me),
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorScheme.onBackground,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                text = stringResource(R.string.login_button),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onLoginClick()
                }
            )

            Spacer(modifier = Modifier.height(80.dp))
        }

        // Dòng ở cuối màn hình, luôn hiện
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom =50.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.dont_have_an_account),
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.sign_up_bottom),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                modifier = Modifier.clickable{
                    onSignUpClick()
                }
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(modifier = Modifier)
}


