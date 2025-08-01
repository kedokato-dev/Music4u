package com.kedokato.lession6.database.Entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val songId: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val albumArt: ByteArray?
)
