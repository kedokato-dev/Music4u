package com.kedokato.music4u.domain.usecase

import com.kedokato.music4u.domain.model.Song
import com.kedokato.music4u.domain.repository.DownloadSongFromRemoteRepo

class LoadDownloadedSongsUseCase(
    private val downloadSongFromRemoteRepo: DownloadSongFromRemoteRepo
) {
    suspend operator fun invoke(userId: Long): List<Song> {
        return downloadSongFromRemoteRepo.getDownloadedSongs(userId)
    }
}