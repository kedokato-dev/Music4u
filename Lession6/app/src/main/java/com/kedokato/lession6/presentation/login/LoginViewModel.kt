package com.kedokato.lession6.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.data.session.SessionManager
import com.kedokato.lession6.domain.usecase.SetUserIdUseCase
import com.kedokato.lession6.domain.usecase.UserAuthenticationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val userAuthenticationUseCase: UserAuthenticationUseCase,
    private val setUserIdUseCase: SetUserIdUseCase
) : ViewModel(){
    private val _state = MutableStateFlow(LoginState())
    val state : StateFlow<LoginState> = _state.asStateFlow()

    private val _navigation = MutableStateFlow<LoginNavigation?>(null)
    val navigation: StateFlow<LoginNavigation?> = _navigation.asStateFlow()

    fun onNavigationHandled() {
        _navigation.value = null
    }


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
               viewModelScope.launch {
                   _navigation.emit(LoginNavigation.OnClickSignUp)
               }
            }
        }
    }

    private suspend fun submitForm() {
        if (validateInputs()) {

            val user = withContext(Dispatchers.IO) {
                userAuthenticationUseCase(
                    username = _state.value.username,
                    password = _state.value.password,
                )
            }

            if(user!=null){
                _navigation.emit(LoginNavigation.OnClickLogin)
                setUserIdUseCase.invoke(user.userId)
                _state.value = _state.value.copy(
                    loginSuccess = true
                )
            }else{
                _state.value = _state.value.copy(loginSuccess = false)
            }

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