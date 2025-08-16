package com.kedokato.music4u.presentation.signup

sealed class SignUpNavigationEvent {
    data object NavigateToLogin : SignUpNavigationEvent()
    data object OnBackClick : SignUpNavigationEvent()
}