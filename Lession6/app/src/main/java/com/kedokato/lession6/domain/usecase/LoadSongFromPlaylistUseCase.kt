package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.data.local.database.Entity.SongEntity
import com.kedokato.lession6.domain.repository.PlaylistRepo

class LoadSongFromPlaylistUseCase(
    private val repo: PlaylistRepo
) {
    suspend operator fun invoke(playlistId: Long): List<SongEntity> {
        return repo.loadSongsFromPlaylist(playlistId)
    }

}