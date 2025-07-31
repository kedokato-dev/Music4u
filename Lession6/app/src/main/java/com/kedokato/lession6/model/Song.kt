package com.kedokato.lession6.model

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

data class Song(
    val id: Long,
    val name: String,
    val artist: String,
    val duration: String,
    val image: ByteArray?,
    val uri: Uri?
)
