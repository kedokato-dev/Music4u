package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.data.local.database.Entity.SongEntity
import com.kedokato.music4u.domain.repository.PlaylistRepo

class AddSongToPlaylistUseCase(private val repo: PlaylistRepo) {
    suspend operator fun invoke(playlistId: Long, song: SongEntity) {
        repo.addSong(song)
        repo.addSongToPlaylist(playlistId, song.songId)
    }
}
