package com.kedokato.lession6.presentation.player

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerMusicViewModel: ViewModel() {
    private val _playerMusicState = MutableStateFlow(PlayerMusicState())
    val playerMusicState: StateFlow<PlayerMusicState> = _playerMusicState.asStateFlow()

    fun processIntent(intent: PlayerMusicIntent) {
        when (intent) {
            is PlayerMusicIntent.OnNextClick -> TODO()
            is PlayerMusicIntent.OnPlayPauseClick -> TODO()
            is PlayerMusicIntent.OnPreviousClick -> TODO()
            is PlayerMusicIntent.OnRepeatClick -> TODO()
            is PlayerMusicIntent.OnSeekTo -> TODO()
            is PlayerMusicIntent.OnShuffleClick -> TODO()
        }
    }




}