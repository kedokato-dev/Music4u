package com.kedokato.lession6.presentation.login

sealed class LoginIntent {
    data class UserNameChanged(val username: String) : LoginIntent()
    data class PasswordChanged(val password: String) : LoginIntent()
    object Submit : LoginIntent()
    object ClearErrors : LoginIntent()
    object RememberMeChanged : LoginIntent()
    object SignUpClicked : LoginIntent()
}