package com.kedokato.lession6.data.mapper

import com.kedokato.lession6.data.remote.respone.ArtistResponse
import com.kedokato.lession6.domain.model.ArtistInfo

fun ArtistResponse.toArtistInfo(): ArtistInfo {
    val imageUrl = image.firstOrNull { it.size == "large" }?.text ?: ""
    return ArtistInfo(
        name = name,
        imageUrl = imageUrl
    )
}