package com.kedokato.music4u.data.repository

import com.kedokato.music4u.data.service.MusicServiceController
import com.kedokato.music4u.domain.model.PlayerState
import com.kedokato.music4u.domain.model.Song
import com.kedokato.music4u.domain.repository.MusicRepo
import kotlinx.coroutines.flow.Flow

class MusicRepositoryImpl(
    private val musicServiceController: MusicServiceController
) : MusicRepo {

    override fun getPlayerState(): Flow<PlayerState> =
        musicServiceController.playerState

    override suspend fun playSong(song: Song) {
        musicServiceController.play(song)
    }

    override suspend fun playPlaylist(
        songs: List<Song>,
        startIndex: Int
    ) {
        musicServiceController.playPlaylist(songs, startIndex)
    }

    override suspend fun pauseSong() {
        musicServiceController.pause()
    }

    override suspend fun resumeSong() {
        musicServiceController.resume()
    }

    override suspend fun nextSong() {
        musicServiceController.skipToNextSong()
    }

    override suspend fun prevSong() {
        musicServiceController.skipToPreviousSong()
    }

    override suspend fun stopSong() {
        musicServiceController.stopSong()
    }

    override suspend fun playFromPlaylist(index: Int) {
        musicServiceController.playFromPlaylist(index)
    }

    override suspend fun toggleRepeatMode() {
        musicServiceController.toggleRepeatMode()
    }

    override suspend fun toggleShuffleMode() {
        musicServiceController.toggleShuffleMode()
    }

    override suspend fun seekTo(position: Long) {
        musicServiceController.seekTo(position)
    }
}