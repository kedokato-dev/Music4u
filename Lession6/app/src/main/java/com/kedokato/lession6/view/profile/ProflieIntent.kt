package com.kedokato.lession6.view.profile

sealed class ProflieIntent {
    data class NameChanged(val name: String) : ProflieIntent()
    data class PhoneChanged(val phone: String) : ProflieIntent()
    data class UniversityChanged(val email: String) : ProflieIntent()
    data class DescriptionChanged(val description: String) : ProflieIntent()

    data object Submit : ProflieIntent()
    data object isEdit : ProflieIntent()
    data object onBackPressed : ProflieIntent()
    data object ChangeAvatar : ProflieIntent()
}