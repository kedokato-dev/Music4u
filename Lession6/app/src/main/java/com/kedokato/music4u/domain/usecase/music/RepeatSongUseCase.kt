package com.kedokato.music4u.domain.usecase.music

import com.kedokato.music4u.domain.repository.MusicRepo

class RepeatSongUseCase(
    private val repository: MusicRepo
) {
    suspend operator fun invoke() {
        repository.toggleRepeatMode()
    }
}