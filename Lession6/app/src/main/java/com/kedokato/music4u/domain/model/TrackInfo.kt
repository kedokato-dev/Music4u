package com.kedokato.music4u.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackInfo(
    val name: String,
    val artist: String,
    val countListeners: String,
    val imageUrl: String
)