package com.kedokato.music4u.domain.repository

import com.kedokato.music4u.domain.model.Song

interface SongLocalDataSource {
    suspend fun getAllSongs(): List<Song>
}
