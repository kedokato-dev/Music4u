package com.kedokato.music4u.data.mapper

import com.kedokato.music4u.data.remote.respone.TrackResponse
import com.kedokato.music4u.domain.model.TrackInfo

fun TrackResponse.toTrackInfo(): TrackInfo {
    val imageUrl = image.firstOrNull { it.size == "large" }?.text ?: ""
    return TrackInfo(
        name = name,
        artist = artist.name,
        countListeners = listeners,
        imageUrl = imageUrl
    )
}