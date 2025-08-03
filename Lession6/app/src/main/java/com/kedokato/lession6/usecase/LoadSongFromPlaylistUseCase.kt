package com.kedokato.lession6.usecase

import com.kedokato.lession6.database.Entity.SongEntity
import com.kedokato.lession6.repository.PlaylistRepo

class LoadSongFromPlaylistUseCase(
    private val repo: PlaylistRepo
) {
    suspend operator fun invoke(playlistId: Long): List<SongEntity> {
        return repo.loadSongsFromPlaylist(playlistId)
    }

}