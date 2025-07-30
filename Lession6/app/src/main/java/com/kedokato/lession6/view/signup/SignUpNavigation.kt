package com.kedokato.lession6.view.signup

sealed class SignUpNavigationEvent {
    data object NavigateToLogin : SignUpNavigationEvent()
}