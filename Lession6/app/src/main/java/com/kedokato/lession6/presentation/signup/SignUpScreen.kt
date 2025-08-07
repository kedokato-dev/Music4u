package com.kedokato.lession6.presentation.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSignUpClick: (String, String) -> Unit = { _, _ -> },
) {
    val viewModel: SignUpViewModel = koinViewModel()
    val colorScheme = getCurrentColorScheme()
    val state by viewModel.state.collectAsState()

    BackHandler {
        onBackClick()
    }

    LaunchedEffect(Unit) {
        viewModel.navigation.collectLatest { event ->
            when (event) {
                is SignUpNavigationEvent.NavigateToLogin -> {
                    onSignUpClick(state.username, state.password)
                }

                is SignUpNavigationEvent.OnBackClick -> {
                    onBackClick()
                }
            }
        }
    }

    SignUpScreenContent(
        modifier = modifier,
        state = state,
        onBackClick = onBackClick,
        onUsernameChanged = { viewModel.processIntent(SignUpIntent.UsernameChanged(it)) },
        onPasswordChanged = { viewModel.processIntent(SignUpIntent.PasswordChanged(it)) },
        onConfirmPasswordChanged = { viewModel.processIntent(SignUpIntent.ConfirmPasswordChanged(it)) },
        onEmailChanged = { viewModel.processIntent(SignUpIntent.EmailChanged(it)) },
        onSubmit = { viewModel.processIntent(SignUpIntent.Submit) }
    )
}

// ------ UI CONTENT ONLY --------

@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    state: SignUpState,
    onBackClick: () -> Unit = {},
    onUsernameChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onConfirmPasswordChanged: (String) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
) {
    val colorScheme = getCurrentColorScheme()

    Scaffold(
        modifier = modifier.fillMaxSize()
            .padding(top = 16.dp),
        containerColor = colorScheme.background,
        topBar = {
            SignupTopAppBar(onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(vertical = paddingValues.calculateTopPadding())
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Logo(modifier = modifier.size(200.dp))

            Text(
                text = stringResource(R.string.sign_up_bottom),
                modifier = modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
                color = colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = modifier.size(32.dp))

            TextInputField(
                value = state.username,
                onValueChange = onUsernameChanged,
                label = stringResource(R.string.username),
                icon = R.drawable.person,
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                error = state.usernameError ?: ""
            )

            Text(
                text = state.usernameError ?: "",
                color = colorScheme.error,
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = modifier.size(16.dp))

            TextInputFieldPassword(
                value = state.password,
                onValueChange = onPasswordChanged,
                label = stringResource(R.string.password),
                icon = R.drawable.lock,
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),

            )

            Text(
                text = state.passwordError ?: "",
                color = colorScheme.error,
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = modifier.size(16.dp))

            TextInputFieldPassword(
                value = state.confirmPassword,
                onValueChange = onConfirmPasswordChanged,
                label = stringResource(R.string.password),
                icon = R.drawable.lock,
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Text(
                text = state.confirmPasswordError ?: "",
                color = colorScheme.error,
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = modifier.size(16.dp))

            TextInputField(
                value = state.email,
                onValueChange = onEmailChanged,
                label = stringResource(R.string.email),
                icon = R.drawable.mail,
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Text(
                text = state.emailError ?: "",
                color = colorScheme.error,
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = modifier.fillMaxSize(0.6f))

            Button(
                text = stringResource(R.string.sign_up_bottom),
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = onSubmit
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupTopAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val colorScheme = getCurrentColorScheme()
    TopAppBar(
        title = { Text("") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.background,
            scrolledContainerColor = colorScheme.background,
            titleContentColor = colorScheme.background
        ),
        windowInsets = WindowInsets(0, 0, 0, 0),
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                modifier = modifier
                    .size(24.dp)
                    .clickable { onBackClick() },
                tint = colorScheme.onBackground
            )
        },
    )
}

// ------ PREVIEW --------

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val fakeState = SignUpState(
        username = "UserTest",
        password = "123456",
        confirmPassword = "123456",
        email = "user@example.com",
        usernameError = null,
        passwordError = null,
        confirmPasswordError = null,
        emailError = null
    )

    SignUpScreenContent(
        state = fakeState,
        onBackClick = {},
        onUsernameChanged = {},
        onPasswordChanged = {},
        onConfirmPasswordChanged = {},
        onEmailChanged = {},
        onSubmit = {}
    )
}
