package com.kedokato.music4u.data.mapper

import com.kedokato.music4u.data.remote.respone.AlbumResponse
import com.kedokato.music4u.domain.model.AlbumInfo

fun AlbumResponse.toAlbumInfo(): AlbumInfo {
    val imageUrl = image.firstOrNull { it.size == "large" }?.text ?: ""
    return AlbumInfo(
        name = name,
        artist = artist.name,
        imageUrl = imageUrl
    )
}
