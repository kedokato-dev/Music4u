package com.kedokato.lession6.data.service

import com.kedokato.lession6.domain.model.PlayerState
import com.kedokato.lession6.domain.model.Song
import kotlinx.coroutines.flow.Flow


interface MusicServiceController {
    val playerState: Flow<PlayerState>
    suspend fun play(song: Song)
    suspend fun playPlaylist(playlist: List<Song>, startIndex: Int)
    suspend fun pause()
    suspend fun resume()
    suspend fun seekTo(position: Long)
    suspend fun stopSong()

    suspend fun skipToNextSong()
    suspend fun skipToPreviousSong()
    suspend fun playFromPlaylist(index: Int)
    suspend fun toggleRepeatMode()
    suspend fun toggleShuffleMode()

}