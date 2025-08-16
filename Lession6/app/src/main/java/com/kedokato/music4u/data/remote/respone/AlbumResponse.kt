package com.kedokato.music4u.data.remote.respone

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumResponse(
    val name: String,
    val artist: Artist,
    val image: List<Image>
)

@Serializable
data class Artist(
    val name: String
)

@Serializable
data class Image(
    @SerializedName("#text") val text: String,
    val size: String
)

data class TopAlbumsResponse(
    val topalbums: TopAlbumsContainer
)

data class TopAlbumsContainer(
    val album: List<AlbumResponse>
)
