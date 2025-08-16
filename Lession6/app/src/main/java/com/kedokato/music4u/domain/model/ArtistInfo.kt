package com.kedokato.music4u.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtistInfo(
    val name: String,
    val imageUrl: String
)