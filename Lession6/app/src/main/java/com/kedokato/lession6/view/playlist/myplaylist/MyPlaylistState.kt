package com.kedokato.lession6.view.playlist.myplaylist

import com.kedokato.lession6.database.Entity.PlaylistWithSongs

data class MyPlaylistState (
    val playlists: List<PlaylistWithSongs> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEmpty: Boolean = false,
    val showDialog: Boolean = false,
)