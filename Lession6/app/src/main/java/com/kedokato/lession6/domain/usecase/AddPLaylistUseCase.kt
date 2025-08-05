package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.data.local.database.Entity.PlaylistEntity
import com.kedokato.lession6.domain.repository.PlaylistRepo

class AddPlaylistUseCase(private val repo: PlaylistRepo) {
    suspend operator fun invoke(playlist: PlaylistEntity): Long {
        return repo.addPlaylist(playlist)
    }
}
