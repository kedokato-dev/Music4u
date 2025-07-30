package com.kedokato.lession6.view.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.component.Button
import com.kedokato.lession6.component.Logo
import com.kedokato.lession6.component.TextInputField
import com.kedokato.lession6.component.TextInputFieldPassword
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    modifier: Modifier,
    viewModel: SignUpViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onSignUpClick: (String, String) -> Unit = { _, _ -> },
) {
    val colorScheme = getCurrentColorScheme()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigation.collectLatest {
            event -> when
                (event) {
                is SignUpNavigationEvent.NavigateToLogin -> {
                    onSignUpClick(state.username, state.password)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            SignupTopAppBar(
                modifier = modifier,
                onBackClick = onBackClick
            )
        }

    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorScheme.background),

            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize(1f)
                    .background(color = colorScheme.background)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Logo(
                    modifier = modifier.size(200.dp)
                )
                Text(
                    text = stringResource(R.string.login_title),
                    modifier = modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = modifier.size(32.dp))

                TextInputField(
                    value = state.username,
                    onValueChange = {
                      viewModel.processIntent(SignUpIntent.UsernameChanged(it))
                    },
                    label = stringResource(R.string.username),
                    icon = R.drawable.person,
                    modifier = modifier.fillMaxWidth(1f),
                    error = state.usernameError ?: ""
                )

                Text(
                    text = state.usernameError ?: "",
                    color = colorScheme.error,
                    modifier = modifier.fillMaxWidth(1f),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = modifier.size(16.dp))

                TextInputFieldPassword(
                    value = state.password,
                    onValueChange = {
                        viewModel.processIntent(SignUpIntent.PasswordChanged(it))
                    },
                    label = stringResource(R.string.password),
                    icon = R.drawable.lock,
                    modifier = modifier.fillMaxWidth(1f)
                )

                Text(
                    text = state.passwordError ?: "",
                    color = colorScheme.error,
                    modifier = modifier.fillMaxWidth(1f),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = modifier.size(16.dp))

                TextInputFieldPassword(
                    value = state.confirmPassword,
                    onValueChange = {
                        viewModel.processIntent(SignUpIntent.ConfirmPasswordChanged(it))
                    },
                    label = stringResource(R.string.password),
                    icon = R.drawable.lock,
                    modifier = modifier.fillMaxWidth(1f)
                )

                Text(
                    text = state.confirmPasswordError ?: "",
                    color = colorScheme.error,
                    modifier = modifier.fillMaxWidth(1f),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = modifier.size(16.dp))

                TextInputField(
                    value = state.email,
                    onValueChange = {
                        viewModel.processIntent(SignUpIntent.EmailChanged(it))
                    },
                    label = stringResource(R.string.email),
                    icon = R.drawable.person,
                    modifier = modifier.fillMaxWidth(1f)
                )

                Text(
                    text = state.emailError ?: "",
                    color = colorScheme.error,
                    modifier = modifier.fillMaxWidth(1f),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = modifier.fillMaxSize(0.6f))


                Button(
                    text = stringResource(R.string.sign_up_bottom),
                    modifier = modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.processIntent(SignUpIntent.Submit)
                    }
                )

            }
        }
    }
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SignupTopAppBar(modifier: Modifier,
                        onBackClick: () -> Unit = {}) {
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
            navigationIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    modifier = modifier.size(24.dp)
                        .clickable{
                            onBackClick()
                        },
                    tint = colorScheme.onBackground
                )
            },
        )
    }

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    Column {
        SignupTopAppBar(modifier = Modifier)
        SignUpScreen(modifier = Modifier)
    }
}