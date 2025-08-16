package com.kedokato.music4u.domain.usecase.music

import com.kedokato.music4u.domain.repository.MusicRepo

class StopSongUseCase(
    private val repository: MusicRepo
) {
    suspend operator fun invoke() {
        repository.stopSong()
    }
}