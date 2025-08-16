package com.kedokato.music4u.domain.model

import android.net.Uri

data class User (
    val userId: Long = 0L,
    val username: String = "",
    val password: String = "",
    val avatarUri: Uri? = null,
    val name: String = "",
    val phone: String? = null,
    val email: String = "",
    val university: String? = null,
    val description: String? = null
)