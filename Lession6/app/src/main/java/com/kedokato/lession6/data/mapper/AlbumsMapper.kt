package com.kedokato.lession6.data.mapper

import com.kedokato.lession6.data.remote.respone.AlbumResponse
import com.kedokato.lession6.domain.model.AlbumInfo

fun AlbumResponse.toAlbumInfo(): AlbumInfo {
    val imageUrl = image.firstOrNull { it.size == "large" }?.text ?: ""
    return AlbumInfo(
        name = name,
        artist = artist.name,
        imageUrl = imageUrl
    )
}
