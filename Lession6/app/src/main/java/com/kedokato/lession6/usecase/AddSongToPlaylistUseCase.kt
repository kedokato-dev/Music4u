package com.kedokato.lession6.usecase

import com.kedokato.lession6.database.Entity.SongEntity
import com.kedokato.lession6.repository.PlaylistRepo

class AddSongToPlaylistUseCase(private val repo: PlaylistRepo) {
    suspend operator fun invoke(playlistId: Long, song: SongEntity) {
        repo.addSong(song)
        repo.addSongToPlaylist(playlistId, song.songId)
    }
}
