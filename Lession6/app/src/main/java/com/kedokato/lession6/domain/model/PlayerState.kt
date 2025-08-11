package com.kedokato.lession6.domain.model

data class PlayerState(
    val song: Song? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val progress: Float = 0f,
    val isLoading: Boolean = false
)