package com.kedokato.lession6.data.repository

import com.kedokato.lession6.data.service.MusicServiceController
import com.kedokato.lession6.domain.model.PlayerState
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.MusicRepo
import kotlinx.coroutines.flow.Flow

class MusicRepositoryImpl(
    private val musicServiceController: MusicServiceController
) : MusicRepo {

    override fun getPlayerState(): Flow<PlayerState> =
        musicServiceController.playerState

    override suspend fun playSong(song: Song) {
        musicServiceController.play(song)
    }

    override suspend fun pauseSong() {
        musicServiceController.pause()
    }

    override suspend fun resumeSong() {
        musicServiceController.resume()
    }

    override suspend fun nextSong() {
        musicServiceController.next()
    }

    override suspend fun prevSong() {
        musicServiceController.prev()
    }

    override suspend fun stopSong() {
        musicServiceController.stopSong()
    }

    override suspend fun seekTo(position: Long) {
        musicServiceController.seekTo(position)
    }
}