package com.kedokato.lession6.data.remote.respone

import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    val name: String,
    val playcount: String,
    val listeners: String,
    val mbid: String,
    val url: String,
    val streamable: String,
    val image: List<Image>
)

data class TopArtistsResponse(
    @SerializedName("artists") val artists: TopArtistsContainer
)

data class TopArtistsContainer(
    @SerializedName("artist") val artist: List<ArtistResponse>
)