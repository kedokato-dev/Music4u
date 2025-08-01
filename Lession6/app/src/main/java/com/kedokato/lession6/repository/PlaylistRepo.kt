package com.kedokato.lession6.repository

import com.kedokato.lession6.model.Song

interface  PlaylistRepo {
    suspend fun getSongsFromStorage(): List<Song>


    suspend fun getAllPlaylists(): List<Long>

    suspend fun insertSong(song: Song): List<Song>
}