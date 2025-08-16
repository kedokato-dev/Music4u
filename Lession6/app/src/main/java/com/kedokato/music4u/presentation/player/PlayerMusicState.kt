package com.kedokato.music4u.presentation.player

import com.kedokato.music4u.domain.model.Song

data class PlayerMusicState (
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val isRepeatMode: Boolean = false,
    val isShuffleMode: Boolean = false,
    val currentSongId: String? = null,
    val progress: Float = 0f,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val songs: List<String> = emptyList(),
    val song: Song? = null,
    val errorMessage: String? = null
)