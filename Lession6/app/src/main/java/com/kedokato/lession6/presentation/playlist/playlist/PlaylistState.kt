package com.kedokato.lession6.presentation.playlist.playlist

import com.kedokato.lession6.domain.model.Song

data class PlaylistState (
    val isSorting: Boolean = false,
    val displayType: Boolean = true,
    val songs : List<Song> = emptyList(),
    val isLoading: Boolean = false,
)