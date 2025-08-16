package com.kedokato.music4u.domain.repository

import com.kedokato.music4u.domain.model.Song

interface GetSongFromRemoteRepo {
    suspend fun getSongFromRemote(): List<Song>
}