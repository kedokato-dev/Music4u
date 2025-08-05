package com.kedokato.lession6.presentation.signup

sealed class SignUpIntent {
    data class EmailChanged(val email: String) : SignUpIntent()
    data class UsernameChanged(val username: String) : SignUpIntent()
    data class PasswordChanged(val password: String) : SignUpIntent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpIntent()
    object Submit : SignUpIntent()
    object ClearErrors : SignUpIntent()
}

