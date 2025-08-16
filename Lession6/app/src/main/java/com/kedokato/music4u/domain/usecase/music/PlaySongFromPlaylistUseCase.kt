package com.kedokato.music4u.domain.usecase.music

import com.kedokato.music4u.domain.repository.MusicRepo

class PlaySongFromPlaylistUseCase(
    private val repository: MusicRepo,
) {
    suspend operator fun invoke(index: Int) {
        repository.playFromPlaylist(index)
    }
}