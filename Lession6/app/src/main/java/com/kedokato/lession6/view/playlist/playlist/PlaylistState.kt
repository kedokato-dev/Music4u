package com.kedokato.lession6.view.playlist.playlist

import com.kedokato.lession6.database.Entity.SongEntity
import com.kedokato.lession6.model.Song

data class PlaylistState (
    val isSorting: Boolean = false,
    val displayType: Boolean = true,
    val songs : List<Song> = emptyList(),
    val isLoading: Boolean = false,
)