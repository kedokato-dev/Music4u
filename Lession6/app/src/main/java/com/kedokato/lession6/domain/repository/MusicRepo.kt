package com.kedokato.lession6.domain.repository

import com.kedokato.lession6.domain.model.PlayerState
import com.kedokato.lession6.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface MusicRepo {
    fun getPlayerState(): Flow<PlayerState>
    suspend fun playSong(song: Song)
    suspend fun pauseSong()
    suspend fun resumeSong()
    suspend fun nextSong()
    suspend fun prevSong()
    suspend fun seekTo(position: Long)


}