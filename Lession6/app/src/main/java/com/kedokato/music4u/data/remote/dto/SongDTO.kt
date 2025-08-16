package com.kedokato.music4u.data.remote.dto

data class SongDTO(
    val title: String,
    val artist: String,
    val kind: String,
    val duration: Long,
    val path: String,
)