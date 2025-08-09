package com.kedokato.lession6.presentation.playlist.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.usecase.LoadSongFromPlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val loadSongsFromPlaylistUseCase: LoadSongFromPlaylistUseCase
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

            is PlaylistIntent.DeleteSongInPlaylist -> {
            }

            is PlaylistIntent.LoadSongs -> {
                loadSongsFromPlaylist(intent.playlistId)
            }
        }
    }


    private fun loadSong(){

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

    private fun loadSongsFromPlaylist(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update { it.copy(isLoading = true) }
                val songs = loadSongsFromPlaylistUseCase(playlistId)
                // Shorten titles and format durations
                val formattedSongs = songs.map { song ->
                    Song(
                        id = song.songId,
                        name = shortenTitle(song.title),
                        artist = song.artist,
                        duration = formatDuration(song.duration),
                        image = song.albumArt,
                        uri = song.uri.toString(),
                    )
                }
                _state.update { it.copy(songs = formattedSongs) }
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                Log.e("PlaylistViewModel", "Error loading songs from playlist", e)
            }
        }
    }


}