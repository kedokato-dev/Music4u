package com.kedokato.lession6.view.playlist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlaylistViewModel : ViewModel(){
    private val _state = MutableStateFlow(PlaylistState())
    val state: StateFlow<PlaylistState> = _state.asStateFlow()


    fun processIntent(intent: PlaylistIntent) {
        when (intent) {
            is PlaylistIntent.Sort -> {
                _state.value = _state.value.copy(isSorting = intent.isSorting)
            }
            is PlaylistIntent.DisplayType -> {
                _state.value = _state.value.copy(displayType = intent.displayType)
            }
            is PlaylistIntent.DeletePlaylist -> {
                // Handle delete playlist logic here
            }
        }
    }


}