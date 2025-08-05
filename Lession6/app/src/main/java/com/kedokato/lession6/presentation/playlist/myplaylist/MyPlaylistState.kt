package com.kedokato.lession6.presentation.playlist.myplaylist

import com.kedokato.lession6.data.local.database.Entity.PlaylistWithSongs

data class MyPlaylistState (
    val playlists: List<PlaylistWithSongs> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEmpty: Boolean = false,
    val showDialog: Boolean = false,
)