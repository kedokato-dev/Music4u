package com.kedokato.lession6.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun saveAlbumArtToCache(context: Context, byteArray: ByteArray, songId: Long): Uri {
    val file = File(context.cacheDir, "album_art_$songId.jpg")
    file.outputStream().use { it.write(byteArray) }
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}
