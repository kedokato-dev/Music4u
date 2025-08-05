package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.data.local.database.Entity.PlaylistWithSongs
import com.kedokato.lession6.domain.repository.PlaylistRepo


class LoadPlaylistUseCase (private val repo: PlaylistRepo){
    suspend  operator fun invoke(userId: Long): List<PlaylistWithSongs> {
        return repo.getAllPlaylistsWithSongs(userId)
    }
}