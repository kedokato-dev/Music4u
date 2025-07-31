package com.kedokato.lession6.repository

import com.kedokato.lession6.model.Song

interface  PlaylistRepo {
    suspend fun getSongsFromStorage(): List<Song>
}