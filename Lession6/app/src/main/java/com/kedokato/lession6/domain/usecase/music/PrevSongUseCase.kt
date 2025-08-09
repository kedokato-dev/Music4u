package com.kedokato.lession6.domain.usecase.music

import com.kedokato.lession6.domain.repository.MusicRepo


class PrevSongUseCase(
    private val repository: MusicRepo
) {
    suspend operator fun invoke() {
        repository.prevSong()
    }
}