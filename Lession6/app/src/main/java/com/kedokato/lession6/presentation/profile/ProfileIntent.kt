package com.kedokato.lession6.presentation.profile

import android.net.Uri

sealed class ProfileIntent {
    data class NameChanged(val name: String) : ProfileIntent()
    data class PhoneChanged(val phone: String) : ProfileIntent()
    data class UniversityChanged(val email: String) : ProfileIntent()
    data class DescriptionChanged(val description: String) : ProfileIntent()
    data class AvatarSelected(val uri: Uri?) : ProfileIntent()

    data object Submit : ProfileIntent()
    data object IsEdit : ProfileIntent()
    data object ChangeAvatar : ProfileIntent()
    data object ShowDialog : ProfileIntent()

    data object LoadUserData : ProfileIntent()
}