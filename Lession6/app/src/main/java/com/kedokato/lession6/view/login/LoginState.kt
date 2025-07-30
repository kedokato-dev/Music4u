package com.kedokato.lession6.view.login

data class LoginState(

    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isRememberMe: Boolean = false,
    val loginSuccess: Boolean = false,
)