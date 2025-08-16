package com.kedokato.music4u.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AlbumInfo(
    val name: String,
    val artist: String,
    val imageUrl: String
)
