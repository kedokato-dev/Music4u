package com.kedokato.lession6.domain.repository

import com.kedokato.lession6.data.local.database.Entity.PlaylistEntity
import com.kedokato.lession6.data.local.database.Entity.PlaylistWithSongs
import com.kedokato.lession6.data.local.database.Entity.SongEntity

interface  PlaylistRepo {

    suspend fun addPlaylist(playlist: PlaylistEntity): Long
    suspend fun addSong(song: SongEntity)
    suspend fun addSongToPlaylist(playlistId: Long, songId: Long)
    suspend fun getAllPlaylistsWithSongs(): List<PlaylistWithSongs>

    suspend fun removePlaylist(playlistId: Long)

    suspend fun loadSongsFromPlaylist(playlistId: Long): List<SongEntity>
}