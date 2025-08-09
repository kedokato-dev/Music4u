package com.kedokato.lession6.data.service

import com.kedokato.lession6.domain.model.PlayerState
import com.kedokato.lession6.domain.model.Song
import kotlinx.coroutines.flow.Flow


interface MusicServiceController {
    val playerState: Flow<PlayerState>
    suspend fun play(song: Song)
    suspend fun pause()
    suspend fun resume()
    suspend fun next()
    suspend fun prev()
    suspend fun seekTo(position: Long)
}