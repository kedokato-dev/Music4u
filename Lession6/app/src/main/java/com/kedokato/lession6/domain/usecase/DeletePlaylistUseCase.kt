package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.domain.repository.PlaylistRepo

class DeletePlaylistUseCase(
    private val repo : PlaylistRepo
) {
    suspend operator fun invoke(playlistId: Long) {
        repo.removePlaylist(playlistId)
    }
}