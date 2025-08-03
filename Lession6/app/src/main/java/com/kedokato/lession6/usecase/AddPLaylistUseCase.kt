package com.kedokato.lession6.usecase

import com.kedokato.lession6.database.Entity.PlaylistEntity
import com.kedokato.lession6.repository.PlaylistRepo

class AddPlaylistUseCase(private val repo: PlaylistRepo) {
    suspend operator fun invoke(playlist: PlaylistEntity): Long {
        return repo.addPlaylist(playlist)
    }
}
