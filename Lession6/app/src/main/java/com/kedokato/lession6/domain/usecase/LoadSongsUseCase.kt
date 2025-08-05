package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.SongLocalDataSource

class LoadSongsUseCase(
    private val repo: SongLocalDataSource
) {
    suspend operator fun invoke(): List<Song> {
        return repo.getAllSongs()
    }
}

