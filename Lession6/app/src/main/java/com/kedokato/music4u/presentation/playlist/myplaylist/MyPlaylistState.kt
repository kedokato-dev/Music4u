package com.kedokato.music4u.presentation.playlist.myplaylist

import com.kedokato.music4u.data.local.database.Entity.PlaylistWithSongs

data class MyPlaylistState (
    val playlists: List<PlaylistWithSongs> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEmpty: Boolean = false,
    val showDialog: Boolean = false,
)