package com.kedokato.lession6.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.data.session.SessionManager
import com.kedokato.lession6.domain.usecase.GetUserIdUseCaseShared
import com.kedokato.lession6.domain.usecase.SaveUserIdUseCase
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
    private val setUserIdUseCase: SetUserIdUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val getUserIdUseCaseShared: GetUserIdUseCaseShared
) : ViewModel(){
    private val _state = MutableStateFlow(LoginState())
    val state : StateFlow<LoginState> = _state.asStateFlow()


    private val _loginEvent = MutableStateFlow<LoginEvent?>(null)
    val loginEvent: StateFlow<LoginEvent?> = _loginEvent.asStateFlow()

    fun onNavigationHandled() {
        _loginEvent.value = null
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
                   _loginEvent.emit(LoginEvent.OnClickSignUp)
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

            if(user!=null && _state.value.isRememberMe){
                _loginEvent.emit(LoginEvent.OnClickLogin)
                setUserIdUseCase.invoke(user.userId)
                _state.value = _state.value.copy(
                    loginSuccess = true
                )

                saveUserIdUseCase.invoke(user?.userId ?: 0L)
                val getUserId = withContext(Dispatchers.IO) {
                    getUserIdUseCaseShared.invoke()
                }

                Log.d("LoginViewModel", "Saved user id: ${getUserId}")
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