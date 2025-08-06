package com.kedokato.lession6.domain.repository

import com.kedokato.lession6.domain.model.Song

interface GetSongFromRemoteRepo {
    suspend fun getSongFromRemote(): List<Song>
}