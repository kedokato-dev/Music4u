package com.kedokato.lession6.usecase

import com.kedokato.lession6.repository.PlaylistRepo

class DeletePlaylistUseCase(
    private val repo : PlaylistRepo
) {
    suspend operator fun invoke(playlistId: Long) {
        repo.removePlaylist(playlistId)
    }
}