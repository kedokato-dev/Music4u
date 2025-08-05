package com.kedokato.lession6.data.local.database.Entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "playlist_song",
    primaryKeys = ["playlistId", "songId"],
    indices = [Index(value = ["songId"])]
)
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val songId: Long
)