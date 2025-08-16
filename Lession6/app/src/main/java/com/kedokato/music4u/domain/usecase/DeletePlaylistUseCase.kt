package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.domain.repository.PlaylistRepo

class DeletePlaylistUseCase(
    private val repo : PlaylistRepo
) {
    suspend operator fun invoke(playlistId: Long) {
        repo.removePlaylist(playlistId)
    }
}