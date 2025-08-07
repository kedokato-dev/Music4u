package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.DownloadSongFromRemoteRepo

class LoadDownloadedSongsUseCase(
    private val downloadSongFromRemoteRepo: DownloadSongFromRemoteRepo
) {
    suspend operator fun invoke(userId: Long): List<Song> {
        return downloadSongFromRemoteRepo.getDownloadedSongs(userId)
    }
}