package com.kedokato.music4u.domain.usecase

import androidx.core.net.toUri
import com.kedokato.music4u.data.local.database.Entity.SongEntity
import com.kedokato.music4u.domain.repository.PlaylistRepo
import com.kedokato.music4u.domain.repository.SongLocalDataSource

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
                    albumArt = it.image?.toUri(),
                    uri = it.uri?.toUri()
                )
            )
        }
    }
}
