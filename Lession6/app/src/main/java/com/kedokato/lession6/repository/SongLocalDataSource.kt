package com.kedokato.lession6.repository

import com.kedokato.lession6.model.Song

interface SongLocalDataSource {
    suspend fun getAllSongs(): List<Song>
}
