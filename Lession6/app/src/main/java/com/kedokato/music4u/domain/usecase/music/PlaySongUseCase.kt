package com.kedokato.music4u.domain.usecase.music

import com.kedokato.music4u.domain.model.Song
import com.kedokato.music4u.domain.repository.MusicRepo


class PlaySongUseCase(
    private val repository: MusicRepo
) {
    suspend operator fun invoke(song: Song) {
        repository.playSong(song)
    }
}