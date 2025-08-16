package com.kedokato.music4u.presentation.playlist.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.music4u.domain.model.Song
import com.kedokato.music4u.domain.repository.MusicRepo
import com.kedokato.music4u.domain.usecase.LoadSongFromPlaylistUseCase
import com.kedokato.music4u.domain.usecase.music.NextSongUseCase
import com.kedokato.music4u.domain.usecase.music.PauseSongUseCase
import com.kedokato.music4u.domain.usecase.music.PlaySongUseCase
import com.kedokato.music4u.domain.usecase.music.PrevSongUseCase
import com.kedokato.music4u.domain.usecase.music.ResumeSongUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val loadSongsFromPlaylistUseCase: LoadSongFromPlaylistUseCase,
    private val playSongUseCase: PlaySongUseCase,
    private val pauseSongUseCase: PauseSongUseCase,
    private val nextSongUseCase: NextSongUseCase,
    private val prevSongUseCase: PrevSongUseCase,
    private val resumeSongUseCase: ResumeSongUseCase,
    private val musicRepo: MusicRepo
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

            is PlaylistIntent.PlaySelectSong -> {
                playSelectedSong(intent.songId)
               _state.value.copy(
                   isPlaying = true,
               )
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
                        image = song.albumArt.toString(),
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


    private fun playSelectedSong(songId: Long) {
        viewModelScope.launch {
            val songs = _state.value.songs
            val selectedIndex = songs.indexOfFirst { it.id == songId }

            if (selectedIndex != -1) {
                // Chỉ cần set playlist cho service và play bài được chọn
                musicRepo.playPlaylist(songs, selectedIndex)

                _state.update {
                    it.copy(
                        isPlaying = true,
                        currentPlayingSongId = songId
                    )
                }
            }
        }
    }


}