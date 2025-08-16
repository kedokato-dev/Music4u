package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.domain.model.Song
import com.kedokato.music4u.domain.repository.SongLocalDataSource

class LoadSongsUseCase(
    private val repo: SongLocalDataSource
) {
    suspend operator fun invoke(): List<Song> {
        return repo.getAllSongs()
    }
}

