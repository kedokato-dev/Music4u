package com.kedokato.lession6.database.Entity

import androidx.room.Entity

@Entity(tableName = "playlist_song", primaryKeys = ["playlistId", "songId"])
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val songId: Long
)
