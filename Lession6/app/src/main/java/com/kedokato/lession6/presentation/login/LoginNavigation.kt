package com.kedokato.lession6.presentation.login

sealed class LoginNavigation {
    data object OnClickLogin : LoginNavigation()
    data object OnClickSignUp : LoginNavigation()
}