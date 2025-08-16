package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.domain.model.Song
import com.kedokato.music4u.domain.repository.GetSongFromRemoteRepo

class LoadSongFromRemoteUseCase(
    private val getSongFromRemote: GetSongFromRemoteRepo
) {
    suspend operator fun invoke(): List<Song> {
        return getSongFromRemote.getSongFromRemote()
    }
}