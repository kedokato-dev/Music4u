package com.kedokato.lession6.domain.usecase

import com.kedokato.lession6.data.local.database.Entity.SongEntity
import com.kedokato.lession6.domain.repository.PlaylistRepo
import com.kedokato.lession6.domain.repository.SongLocalDataSource

class ScanAndInsertSongsUseCase(
    private val songLocalDataSource: SongLocalDataSource,
    private val playlistRepo: PlaylistRepo
) {
    suspend operator fun invoke() {
        val songs = songLocalDataSource.getAllSongs()
        songs.forEach {
            playlistRepo.addSong(
                SongEntity(
                    songId = it.id,
                    title = it.name,
                    artist = it.artist,
                    duration = it.duration.toLong(),
                    albumArt = it.image,
                    uri = it.uri
                )
            )
        }
    }
}
