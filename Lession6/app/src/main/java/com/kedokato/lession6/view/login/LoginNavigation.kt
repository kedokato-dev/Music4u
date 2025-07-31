package com.kedokato.lession6.view.login

sealed class LoginNavigation {
    data object OnClickLogin : LoginNavigation()
//    data object NavigateToSignUp : LoginNavigation()
}