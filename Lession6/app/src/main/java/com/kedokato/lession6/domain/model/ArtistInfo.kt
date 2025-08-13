package com.kedokato.lession6.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtistInfo(
    val name: String,
    val imageUrl: String
)