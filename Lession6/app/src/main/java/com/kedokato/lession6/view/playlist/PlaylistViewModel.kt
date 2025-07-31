package com.kedokato.lession6.view.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.model.Song
import com.kedokato.lession6.usecase.LoadSongsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val loadSongsUseCase: LoadSongsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PlaylistState())
    val state: StateFlow<PlaylistState> = _state.asStateFlow()


    fun processIntent(intent: PlaylistIntent) {
        when (intent) {
            is PlaylistIntent.Sort -> {
                _state.value = _state.value.copy(isSorting = intent.isSorting)
            }

            is PlaylistIntent.DisplayType -> {
                _state.value = _state.value.copy(displayType = !intent.displayType)
            }

            is PlaylistIntent.DeletePlaylist -> {
                // Handle delete playlist logic here
            }

            is PlaylistIntent.LoadSongs -> {
                viewModelScope.launch {
                    val songs = loadSongsUseCase()
                    val processedSongs = songs.map { song ->
                        song.copy(name = shortenTitle(song.name, 20))
                        song.copy(artist = shortenTitle(song.artist))
                        song.copy(duration = formatDuration(song.duration.toLong()))
                    }
                    _state.update { it.copy(songs = processedSongs) }
                }
            }
        }
    }

    private fun shortenTitle(title: String, maxLength: Int = 30): String {
        return if (title.length > maxLength) {
            title.take(maxLength).trimEnd() + "..."
        } else {
            title
        }
    }

    private fun formatDuration(durationMs: Long): String {
        val totalSeconds = durationMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }


}