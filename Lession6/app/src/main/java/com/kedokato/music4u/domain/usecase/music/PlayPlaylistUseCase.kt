package com.kedokato.music4u.domain.usecase.music

import com.kedokato.music4u.domain.model.Song
import com.kedokato.music4u.domain.repository.MusicRepo

class PlayPlaylistUseCase(
    private val repo: MusicRepo
) {
    suspend operator fun invoke(songs: List<Song>, startIndex: Int = 0) {
        repo.playPlaylist(songs, startIndex)
    }
}