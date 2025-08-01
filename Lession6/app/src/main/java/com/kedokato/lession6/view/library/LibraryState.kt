package com.kedokato.lession6.view.library

import com.kedokato.lession6.model.Song

data class LibraryState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val songs: List<Song> = emptyList(),
    val requestedPermission: String? = null,
)