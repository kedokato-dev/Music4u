package com.kedokato.music4u.data.mapper

import com.kedokato.music4u.data.remote.respone.ArtistResponse
import com.kedokato.music4u.domain.model.ArtistInfo

fun ArtistResponse.toArtistInfo(): ArtistInfo {
    val imageUrl = image.firstOrNull { it.size == "large" }?.text ?: ""
    return ArtistInfo(
        name = name,
        imageUrl = imageUrl
    )
}