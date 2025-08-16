package com.kedokato.music4u.domain.repository

import com.kedokato.music4u.data.local.database.Entity.PlaylistEntity
import com.kedokato.music4u.data.local.database.Entity.PlaylistWithSongs
import com.kedokato.music4u.data.local.database.Entity.SongEntity

interface  PlaylistRepo {

    suspend fun addPlaylist(playlist: PlaylistEntity): Long
    suspend fun addSong(song: SongEntity)
    suspend fun addSongToPlaylist(playlistId: Long, songId: Long)
    suspend fun getAllPlaylistsWithSongs(userId: Long): List<PlaylistWithSongs>

    suspend fun removePlaylist(playlistId: Long)

    suspend fun loadSongsFromPlaylist(playlistId: Long): List<SongEntity>
}