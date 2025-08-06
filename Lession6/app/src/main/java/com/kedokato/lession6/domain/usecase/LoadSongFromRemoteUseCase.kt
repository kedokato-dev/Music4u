package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.GetSongFromRemoteRepo

class LoadSongFromRemoteUseCase(
    private val getSongFromRemote: GetSongFromRemoteRepo
) {
    suspend operator fun invoke(): List<Song> {
        return getSongFromRemote.getSongFromRemote()
    }
}