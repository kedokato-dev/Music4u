package com.kedokato.music4u.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Song(
    val id: Long,
    val name: String,
    val artist: String,
    val duration: String,
    val image: String?,
    val uri: String?
)