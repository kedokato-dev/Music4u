package com.kedokato.lession6.usecase

import com.kedokato.lession6.database.Entity.PlaylistWithSongs
import com.kedokato.lession6.repository.PlaylistRepo


class LoadPlaylistUseCase (private val repo: PlaylistRepo){
    suspend  operator fun invoke(): List<PlaylistWithSongs> {
        return repo.getAllPlaylistsWithSongs()
    }
}