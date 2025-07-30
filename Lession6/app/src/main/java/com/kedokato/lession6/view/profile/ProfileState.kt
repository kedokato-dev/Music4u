package com.kedokato.lession6.view.profile

import android.net.Uri

data class ProfileState(
    val name: String = "",
    val phone: String = "",
    val university: String = "",
    val description: String = "",

    val nameError: String? = null,
    val phoneError: String? = null,
    val universityError: String? = null,
    val descriptionError: String? = null,

    val isSubmitVisible: Boolean = false,

    val inputEnable: Boolean = false,

    val isEdit: Boolean = false,
    val avatarUrl: Uri? = null,
    val showDialog: Boolean = false,
)