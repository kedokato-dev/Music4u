package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.data.local.database.Entity.PlaylistWithSongs
import com.kedokato.music4u.domain.repository.PlaylistRepo


class LoadPlaylistUseCase (private val repo: PlaylistRepo){
    suspend  operator fun invoke(userId: Long): List<PlaylistWithSongs> {
        return repo.getAllPlaylistsWithSongs(userId)
    }
}