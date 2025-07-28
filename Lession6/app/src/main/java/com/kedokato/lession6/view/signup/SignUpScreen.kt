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
import androidx.navigation.compose.rememberNavController
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.component.Button
import com.kedokato.lession6.component.Logo
import com.kedokato.lession6.component.TextInputField
import com.kedokato.lession6.component.TextInputFieldPassword

@Composable
fun SignUpScreen(
    modifier: Modifier,
    onBackClick: () -> Unit = {},
    onSignUpClick: (String, String) -> Unit = { _, _ -> },
) {
    val colorScheme = getCurrentColorScheme()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    val usernameErrorText = stringResource(R.string.username_error)
    val emailErrorText = stringResource(R.string.email_error)
    val passwordErrorText = stringResource(R.string.password_error)
    val confirmPasswordErrorText = stringResource(R.string.confirm_password_error)

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
                    value = username,
                    onValueChange = {
                        username = it
                        usernameError = ""
                    },
                    label = stringResource(R.string.username),
                    icon = R.drawable.person,
                    modifier = modifier.fillMaxWidth(1f),
                    error = usernameError
                )

                Text(
                    text = usernameError,
                    color = colorScheme.error,
                    modifier = modifier.fillMaxWidth(1f),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = modifier.size(16.dp))

                TextInputFieldPassword(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    label = stringResource(R.string.password),
                    icon = R.drawable.lock,
                    modifier = modifier.fillMaxWidth(1f)
                )

                Text(
                    text = passwordError,
                    color = colorScheme.error,
                    modifier = modifier.fillMaxWidth(1f),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = modifier.size(16.dp))

                TextInputFieldPassword(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                    },
                    label = stringResource(R.string.password),
                    icon = R.drawable.lock,
                    modifier = modifier.fillMaxWidth(1f)
                )

                Text(
                    text = confirmPasswordError,
                    color = colorScheme.error,
                    modifier = modifier.fillMaxWidth(1f),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = modifier.size(16.dp))

                TextInputField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = stringResource(R.string.email),
                    icon = R.drawable.person,
                    modifier = modifier.fillMaxWidth(1f)
                )

                Text(
                    text = emailError,
                    color = colorScheme.error,
                    modifier = modifier.fillMaxWidth(1f),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = modifier.fillMaxSize(0.6f))


                Button(
                    text = stringResource(R.string.sign_up_bottom),
                    modifier = modifier.fillMaxWidth(),
                    onClick = {
                        usernameError = if (!isValidUsername(username)) {
                            username = ""
                            usernameErrorText
                        } else {
                            ""
                        }

                        emailError = if (!isValidEmail(email)) {
                            email = ""
                            emailErrorText
                        } else {
                            ""
                        }

                        passwordError = if (!isValidPassword(password)) {
                            password = ""
                            passwordErrorText
                        } else {
                            ""
                        }

                        confirmPasswordError = if (password != confirmPassword) {
                            confirmPassword = ""
                            confirmPasswordErrorText
                        } else {
                            ""
                        }

                        if (usernameError.isEmpty() && emailError.isEmpty() && passwordError.isEmpty() && confirmPasswordError.isEmpty()) {
                            onSignUpClick(username, password)
                        }
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


fun isValidUsername(username: String): Boolean {
    val usernameRegex = Regex("^[a-z0-9]+$", RegexOption.IGNORE_CASE)
    return usernameRegex.matches(username)
}

fun isValidPassword(password: String): Boolean {
    val passwordRegex = Regex("^[A-Za-z0-9]+$")
    return passwordRegex.matches(password)
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[a-z0-9._-]+@apero\\.vn$")
    return emailRegex.matches(email)
}



@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    Column {
        SignupTopAppBar(modifier = Modifier)
        SignUpScreen(modifier = Modifier)
    }
}