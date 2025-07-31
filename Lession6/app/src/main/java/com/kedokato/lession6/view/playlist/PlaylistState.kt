package com.kedokato.lession6.view.playlist

import com.kedokato.lession6.model.Song

data class PlaylistState (
    val isSorting: Boolean = false,
    val displayType: Boolean = true,
    val songs : List<Song> = emptyList()
)