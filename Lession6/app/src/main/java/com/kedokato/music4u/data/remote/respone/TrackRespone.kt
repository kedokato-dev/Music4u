package com.kedokato.music4u.data.remote.respone

import com.google.gson.annotations.SerializedName

data class TrackResponse(
    val name: String,
    val playcount: String,
    val listeners: String,
    val url: String,
    val streamable: String,
    val artist: Artist,
    val image: List<Image>,
    @SerializedName("@attr") val attr: TrackAttr
)

data class TrackAttr(
    val rank: String
)

data class TopTracksResponse(
    val toptracks: TopTracksContainer
)

data class TopTracksContainer(
    val track: List<TrackResponse>
)