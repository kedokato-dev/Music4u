package com.kedokato.lession6.usecase

import com.kedokato.lession6.model.Song
import com.kedokato.lession6.repository.PlaylistRepo
import com.kedokato.lession6.repository.SongLocalDataSource

class LoadSongsUseCase(
    private val repo: SongLocalDataSource
) {
    suspend operator fun invoke(): List<Song> {
        return repo.getAllSongs()
    }
}

