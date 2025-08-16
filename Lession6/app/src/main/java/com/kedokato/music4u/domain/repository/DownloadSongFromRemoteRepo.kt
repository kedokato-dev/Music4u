package com.kedokato.music4u.domain.repository

import com.kedokato.music4u.domain.model.Song

interface DownloadSongFromRemoteRepo{
    suspend fun downloadSongFromRemote(songs: List<Song>, userId: Long): List<Song>

    suspend fun checkSongExists(song: Song, userName: String): Boolean

    suspend fun getDownloadedSongs(userId: Long): List<Song>
}