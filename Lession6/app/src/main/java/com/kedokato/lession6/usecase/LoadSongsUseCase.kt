package com.kedokato.lession6.usecase

import com.kedokato.lession6.model.Song
import com.kedokato.lession6.repository.PlaylistRepo

class LoadSongsUseCase(
    private val repo: PlaylistRepo
) {
    suspend operator fun invoke(): List<Song> {
        return repo.getSongsFromStorage()
    }
}

