package com.kedokato.lession6.presentation.playlist.playlist

import com.kedokato.lession6.domain.model.Song

data class PlaylistState (
    val isSorting: Boolean = false,
    val displayType: Boolean = true,
    val songs : List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,

    val song: Song? = null,
    val currentPlayingSongId: Long? = null,
    val isRepeatMode: Boolean = false,
    val isShuffle: Boolean = false,
    val currentSongId: String? = null,
    val progress: Float = 0f,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val errorMessage: String? = null
)