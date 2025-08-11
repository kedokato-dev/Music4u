package com.kedokato.lession6.data.local.database.Entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kedokato.lession6.data.local.database.converters.UriConverter

@Entity(tableName = "songs")
@TypeConverters(UriConverter::class)
data class SongEntity(
    @PrimaryKey val songId: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val albumArt: Uri?,
    val uri: Uri?
)