package com.kedokato.lession6.repoImpl

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.kedokato.lession6.database.DAO.PlaylistDao
import com.kedokato.lession6.database.Entity.PlaylistEntity
import com.kedokato.lession6.database.Entity.PlaylistSongCrossRef
import com.kedokato.lession6.database.Entity.PlaylistWithSongs
import com.kedokato.lession6.database.Entity.SongEntity
import com.kedokato.lession6.model.Song
import com.kedokato.lession6.repository.PlaylistRepo
import java.io.File

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

        override suspend fun getAllPlaylistsWithSongs(): List<PlaylistWithSongs> {
            return dao.getPlaylistsWithSongs()
        }

        override suspend fun removePlaylist(playlistId: Long) {
            return dao.deletePlaylistCompletely(playlistId)
        }

        override suspend fun loadSongsFromPlaylist(playlistId: Long): List<SongEntity> {
            return dao.loadSongsFromPlaylist(playlistId)
        }
    }
