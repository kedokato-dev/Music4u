package com.kedokato.lession6.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.domain.usecase.UserRegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(
    private val userRegisterUseCase: UserRegisterUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    private val _navigation = MutableSharedFlow<SignUpNavigationEvent>()
    val navigation: SharedFlow<SignUpNavigationEvent> = _navigation

    fun processIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.ConfirmPasswordChanged -> {
                clearErrors()
                _state.value = _state.value.copy(
                    confirmPassword = intent.confirmPassword,
                    confirmPasswordError = null
                )
            }

            is SignUpIntent.EmailChanged -> {
                clearErrors()
                _state.value = _state.value.copy(email = intent.email, emailError = null)
            }

            is SignUpIntent.UsernameChanged -> {
                clearErrors()
                _state.value = _state.value.copy(username = intent.username, usernameError = null)
            }

            is SignUpIntent.PasswordChanged -> {
                clearErrors()
                _state.value = _state.value.copy(password = intent.password, passwordError = null)
            }

            SignUpIntent.Submit -> {
                viewModelScope.launch {
                    submitForm()
                }
            }

            SignUpIntent.ClearErrors -> {
                clearErrors()
            }
        }
    }


    private suspend fun submitForm() {
        if (validateInputs()) {
            _state.update {
                it.copy(isLoading = true, generalError = null)
            }

            val register = withContext(Dispatchers.IO) {
                userRegisterUseCase(state.value.username, state.value.password, state.value.email)
            }

            if (register <= 0) {
                _state.update {
                    it.copy(isLoading = false, generalError = "Registration failed")
                }
                return
            }

            _navigation.emit(SignUpNavigationEvent.NavigateToLogin)
        } else {
            _state.update {
                it.copy(generalError = "Please fix the errors above")
            }
        }
    }

    private fun clearErrors() {
        _state.update {
            it.copy(
                emailError = null,
                usernameError = null,
                passwordError = null,
                confirmPasswordError = null,
                generalError = null
            )
        }
    }

    private fun isValidUsername(username: String): Boolean {
        val usernameRegex = Regex("^[a-z0-9]+$", RegexOption.IGNORE_CASE)
        return usernameRegex.matches(username)
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex("^[A-Za-z0-9]+$")
        return passwordRegex.matches(password)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-z0-9._-]+@apero\\.vn$")
        return emailRegex.matches(email)
    }

    private fun validateInputs(): Boolean {
        val currentState = _state.value

        var isValid = true

        var usernameError: String? = null
        var passwordError: String? = null
        var emailError: String? = null
        var confirmPasswordError: String? = null

        // Username
        if (currentState.username.isEmpty()) {
            usernameError = "Username cannot be empty"
            isValid = false
        } else if (!isValidUsername(currentState.username)) {
            usernameError = "Invalid username format"
            isValid = false
        }

        // Password
        if (currentState.password.isEmpty()) {
            passwordError = "Password cannot be empty"
            isValid = false
        } else if (!isValidPassword(currentState.password)) {
            passwordError = "Invalid password format"
            isValid = false
        }

        // Email
        if (currentState.email.isEmpty()) {
            emailError = "Email cannot be empty"
            isValid = false
        } else if (!isValidEmail(currentState.email)) {
            emailError = "Invalid email format"
            isValid = false
        }

        // Confirm password
        if (currentState.confirmPassword.isEmpty()) {
            confirmPasswordError = "Confirm password cannot be empty"
            isValid = false
        } else if (currentState.password != currentState.confirmPassword) {
            confirmPasswordError = "Passwords do not match"
            isValid = false
        }

        _state.update {
            it.copy(
                usernameError = usernameError,
                passwordError = passwordError,
                emailError = emailError,
                confirmPasswordError = confirmPasswordError
            )
        }

        return isValid
    }

}