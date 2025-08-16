package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.data.local.database.Entity.PlaylistEntity
import com.kedokato.music4u.domain.repository.PlaylistRepo

class AddPlaylistUseCase(private val repo: PlaylistRepo) {
    suspend operator fun invoke(playlist: PlaylistEntity): Long {
        return repo.addPlaylist(playlist)
    }
}
