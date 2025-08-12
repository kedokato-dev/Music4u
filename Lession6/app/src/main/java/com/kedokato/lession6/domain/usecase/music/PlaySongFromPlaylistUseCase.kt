package com.kedokato.lession6.domain.usecase.music

import com.kedokato.lession6.domain.repository.MusicRepo

class PlaySongFromPlaylistUseCase(
    private val repository: MusicRepo,
) {
    suspend operator fun invoke(index: Int) {
        repository.playFromPlaylist(index)
    }
}