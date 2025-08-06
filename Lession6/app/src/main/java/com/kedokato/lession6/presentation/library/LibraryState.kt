package com.kedokato.lession6.presentation.library

import com.kedokato.lession6.data.local.database.Entity.SongEntity
import com.kedokato.lession6.domain.model.Song

data class LibraryState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val localSongs: List<Song> = emptyList(),
    val remoteSongs: List<Song> = emptyList(),
    val requestedPermission: String? = null,
    val playlistId: Long? = null,
    val showChoosePlaylistDialog: Boolean = false,
    val song: SongEntity? = null,
    val errorLoadingFromRemote: String? = null,
)