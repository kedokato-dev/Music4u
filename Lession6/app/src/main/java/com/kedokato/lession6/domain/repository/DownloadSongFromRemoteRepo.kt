package com.kedokato.lession6.domain.repository

import com.kedokato.lession6.data.remote.dto.SongDTO
import com.kedokato.lession6.domain.model.Song

interface DownloadSongFromRemoteRepo{
    suspend fun downloadSongFromRemote(songs: List<Song>, userId: Long): List<Song>

    suspend fun checkSongExists(song: Song, userName: String): Boolean

    suspend fun getDownloadedSongs(userId: Long): List<Song>
}