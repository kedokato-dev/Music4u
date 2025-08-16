package com.kedokato.music4u.domain.repository

import com.kedokato.music4u.domain.model.PlayerState
import com.kedokato.music4u.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface MusicRepo {
    fun getPlayerState(): Flow<PlayerState>
    suspend fun playSong(song: Song)
    suspend fun playPlaylist(songs: List<Song>, startIndex: Int = 0)
    suspend fun pauseSong()
    suspend fun resumeSong()
    suspend fun nextSong()
    suspend fun prevSong()
    suspend fun stopSong()
    suspend fun playFromPlaylist(index: Int)
    suspend fun toggleRepeatMode()
    suspend fun toggleShuffleMode()
    suspend fun seekTo(position: Long)


}