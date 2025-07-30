package com.kedokato.lession6.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel(){
    private val _state = MutableStateFlow(LoginState())
    val state : StateFlow<LoginState> = _state.asStateFlow()

    private val _navigation = MutableStateFlow<LoginNavigation?>(null)
    val navigation: StateFlow<LoginNavigation?> = _navigation.asStateFlow()


    fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UserNameChanged -> {
                _state.value = _state.value.copy(
                    username = intent.username,
                    usernameError = null
                )
            }

            is LoginIntent.PasswordChanged -> {
                _state.value = _state.value.copy(
                    password = intent.password,
                    passwordError = null
                )
            }

            LoginIntent.Submit -> {
               viewModelScope.launch {
                   submitForm()
                }
            }

            LoginIntent.ClearErrors -> {
                _state.value = _state.value.copy(
                    usernameError = null,
                    passwordError = null
                )
            }

            LoginIntent.RememberMeChanged -> {
                _state.value = _state.value.copy(
                    isRememberMe = !_state.value.isRememberMe
                )
            }

            LoginIntent.SignUpClicked -> {
                // Handle sign-up navigation or action here
                // For example, you might want to navigate to a sign-up screen
                // _navigation.emit(LoginNavigation.SignUp)
            }
        }
    }

    private suspend fun submitForm() {
        if (validateInputs()) {
            _state.value = _state.value.copy(
                loginSuccess = true
            )

            _navigation.emit(LoginNavigation.OnClickLogin) // Emit navigation event to Home screen
        } else {
            _state.value = _state.value.copy(loginSuccess = false)
        }
    }

    private fun validateInputs(): Boolean {
        val usernameError = if (_state.value.username.isBlank()) "Username cannot be empty" else null
        val passwordError = if (_state.value.password.isBlank()) "Password cannot be empty" else null

        _state.value = _state.value.copy(
            usernameError = usernameError,
            passwordError = passwordError
        )

        return usernameError == null && passwordError == null
    }
}