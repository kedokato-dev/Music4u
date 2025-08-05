package com.kedokato.lession6.presentation.signup

sealed class SignUpNavigationEvent {
    data object NavigateToLogin : SignUpNavigationEvent()
    data object OnBackClick : SignUpNavigationEvent()
}