package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.data.local.database.Entity.PlaylistWithSongs
import com.kedokato.lession6.domain.repository.PlaylistRepo


class LoadPlaylistUseCase (private val repo: PlaylistRepo){
    suspend  operator fun invoke(): List<PlaylistWithSongs> {
        return repo.getAllPlaylistsWithSongs()
    }
}