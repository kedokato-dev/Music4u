package com.kedokato.music4u.data.repository

import com.kedokato.music4u.data.local.database.DAO.PlaylistDao
import com.kedokato.music4u.data.local.database.Entity.PlaylistEntity
import com.kedokato.music4u.data.local.database.Entity.PlaylistSongCrossRef
import com.kedokato.music4u.data.local.database.Entity.PlaylistWithSongs
import com.kedokato.music4u.data.local.database.Entity.SongEntity
import com.kedokato.music4u.domain.repository.PlaylistRepo

class PlaylistRepoImpl(
        private val dao: PlaylistDao
    ) : PlaylistRepo {

        override suspend fun addPlaylist(playlist: PlaylistEntity): Long {
            return dao.insertPlaylist(playlist)
        }

        override suspend fun addSong(song: SongEntity) {
            dao.insertSongs(listOf(song))
        }

        override suspend fun addSongToPlaylist(playlistId: Long, songId: Long) {
            dao.insertPlaylistSongCrossRef(PlaylistSongCrossRef(playlistId, songId))
        }

        override suspend fun getAllPlaylistsWithSongs(userId: Long): List<PlaylistWithSongs> {
            return dao.getPlaylistsWithSongs(userId)
        }

        override suspend fun removePlaylist(playlistId: Long) {
            return dao.deletePlaylistCompletely(playlistId)
        }

        override suspend fun loadSongsFromPlaylist(playlistId: Long): List<SongEntity> {
            return dao.loadSongsFromPlaylist(playlistId)
        }
    }
