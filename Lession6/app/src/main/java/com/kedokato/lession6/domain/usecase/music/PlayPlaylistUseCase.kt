package com.kedokato.lession6.domain.usecase.music

import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.MusicRepo

class PlayPlaylistUseCase(
    private val repo: MusicRepo
) {
    suspend operator fun invoke(songs: List<Song>, startIndex: Int = 0) {
        repo.playPlaylist(songs, startIndex)
    }
}