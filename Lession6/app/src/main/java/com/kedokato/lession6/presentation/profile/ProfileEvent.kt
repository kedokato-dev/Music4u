package com.kedokato.lession6.presentation.profile

sealed class ProfileEvent {
    data object OnBackPressed : ProfileEvent()
    data object ChangeAvatar : ProfileEvent()
    data object ShowDialog : ProfileEvent()
    data object ToggleEditMode : ProfileEvent()

    data object ShowSnackBarError : ProfileEvent()
    data object OpenGallery : ProfileEvent()
}
