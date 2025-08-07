package com.kedokato.lession6.presentation.login

sealed class LoginIntent {
    data class UserNameChanged(val username: String) : LoginIntent()
    data class PasswordChanged(val password: String) : LoginIntent()
    object Submit : LoginIntent()
    object ClearErrors : LoginIntent()
    object RememberMeChanged : LoginIntent()
    object SignUpClicked : LoginIntent()
}

sealed class LoginEvent {
    data class ShowError(val message: String) : LoginEvent()
    data class ShowSuccess(val message: String) : LoginEvent()
    data class IsRememberMeChecked(val isChecked: Boolean) : LoginEvent()

    data object OnClickLogin : LoginEvent()
    data object OnClickSignUp : LoginEvent()
}