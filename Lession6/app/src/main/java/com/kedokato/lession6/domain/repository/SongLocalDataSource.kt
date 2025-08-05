package com.kedokato.lession6.domain.repository

import com.kedokato.lession6.domain.model.Song

interface SongLocalDataSource {
    suspend fun getAllSongs(): List<Song>
}
