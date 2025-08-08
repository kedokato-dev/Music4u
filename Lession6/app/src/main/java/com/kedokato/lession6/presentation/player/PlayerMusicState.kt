package com.kedokato.lession6.presentation.player

data class PlayerMusicState (
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val isRepeatMode: Boolean = false,
    val isShuffle: Boolean = false,
    val currentSongId: String? = null,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val songs: List<String> = emptyList(),
    val errorMessage: String? = null
)