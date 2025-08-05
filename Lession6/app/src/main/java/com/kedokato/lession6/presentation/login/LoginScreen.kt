package com.kedokato.lession6.presentation.login

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.presentation.component.Button
import com.kedokato.lession6.presentation.component.Logo
import com.kedokato.lession6.presentation.component.TextInputField
import com.kedokato.lession6.presentation.component.TextInputFieldPassword
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    initialUsername: String,
    initialPassword: String,
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    val viewModel: LoginViewModel = koinViewModel()

    val colorScheme = getCurrentColorScheme()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(initialUsername, initialPassword) {
        if (initialUsername.isNotEmpty()) {
            viewModel.processIntent(LoginIntent.UserNameChanged(initialUsername))
        }
        if (initialPassword.isNotEmpty()) {
            viewModel.processIntent(LoginIntent.PasswordChanged(initialPassword))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigation.collectLatest { event ->
            when (event) {
                is LoginNavigation.OnClickLogin -> {
                    onLoginClick()
                    viewModel.onNavigationHandled()
                }

                is LoginNavigation.OnClickSignUp -> {
                    onSignUpClick()
                    viewModel.onNavigationHandled()
                }

                null -> {

                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
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
                value = state.username,
                onValueChange = {
                    viewModel.processIntent(LoginIntent.UserNameChanged(it))
                },
                label = stringResource(R.string.username),
                icon = R.drawable.person,
                modifier = modifier.fillMaxWidth(1f)
            )
            Text(
                text = state.usernameError ?: "",
                color = colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextInputFieldPassword(
                value = state.password,
                onValueChange = {
                    viewModel.processIntent(LoginIntent.PasswordChanged(it))
                },
                label = stringResource(R.string.password),
                icon = R.drawable.lock,
                modifier = modifier.fillMaxWidth(1f)
            )

            Text(
                text = state.passwordError ?: "",
                color = colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = state.isRememberMe,
                    onCheckedChange = {
                        viewModel
                            .processIntent(LoginIntent.RememberMeChanged)
                    },
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
                    viewModel.processIntent(LoginIntent.Submit)
                }
            )

            Spacer(modifier = Modifier.height(80.dp))
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp),
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
                    viewModel.processIntent(LoginIntent.SignUpClicked)
                }
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(modifier = Modifier,
        initialUsername = "",
        initialPassword = "",
        onSignUpClick = {},
        onLoginClick = {}
    )
}


