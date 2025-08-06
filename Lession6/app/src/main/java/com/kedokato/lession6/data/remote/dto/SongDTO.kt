package com.kedokato.lession6.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SongDTO(
    val title: String,
    val artist: String,
    val kind: String,
    val duration: Long,
    val path: String,
)