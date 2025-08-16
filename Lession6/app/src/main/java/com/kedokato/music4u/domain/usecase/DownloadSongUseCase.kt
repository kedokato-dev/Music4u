package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.domain.model.Song
import com.kedokato.music4u.domain.repository.DownloadSongFromRemoteRepo

class DownloadSongUseCase(
    private val repo: DownloadSongFromRemoteRepo
) {
    suspend operator fun invoke(songs: List<Song>, userId: Long): List<Song> {
        return repo.downloadSongFromRemote(songs, userId)
    }
}