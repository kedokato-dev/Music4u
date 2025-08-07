package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.data.remote.dto.SongDTO
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.DownloadSongFromRemoteRepo

class DownloadSongUseCase(
    private val repo: DownloadSongFromRemoteRepo
) {
    suspend operator fun invoke(songs: List<Song>, userId: Long): List<Song> {
        return repo.downloadSongFromRemote(songs, userId)
    }
}