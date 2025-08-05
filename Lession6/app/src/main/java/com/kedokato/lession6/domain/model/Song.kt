package com.kedokato.lession6.domain.model

import android.net.Uri

data class Song(
    val id: Long,
    val name: String,
    val artist: String,
    val duration: String,
    val image: ByteArray?,
    val uri: Uri?
)