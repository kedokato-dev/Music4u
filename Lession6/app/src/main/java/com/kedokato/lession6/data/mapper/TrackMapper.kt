package com.kedokato.lession6.data.mapper

import com.kedokato.lession6.data.remote.respone.TrackResponse
import com.kedokato.lession6.domain.model.TrackInfo

fun TrackResponse.toTrackInfo(): TrackInfo {
    val imageUrl = image.firstOrNull { it.size == "large" }?.text ?: ""
    return TrackInfo(
        name = name,
        artist = artist.name,
        countListeners = listeners,
        imageUrl = imageUrl
    )
}