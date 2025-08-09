package com.kedokato.lession6.domain.usecase.music

import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.MusicRepo


class PlaySongUseCase(
    private val repository: MusicRepo
) {
    suspend operator fun invoke(song: Song) {
        repository.playSong(song)
    }
}