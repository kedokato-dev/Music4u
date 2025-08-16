package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.data.local.database.Entity.SongEntity
import com.kedokato.music4u.domain.repository.PlaylistRepo

class LoadSongFromPlaylistUseCase(
    private val repo: PlaylistRepo
) {
    suspend operator fun invoke(playlistId: Long): List<SongEntity> {
        return repo.loadSongsFromPlaylist(playlistId)
    }

}