package com.kedokato.lession6.view.library

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.database.Entity.SongEntity
import com.kedokato.lession6.model.Song
import com.kedokato.lession6.usecase.AddSongToPlaylistUseCase
import com.kedokato.lession6.usecase.LoadSongsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LibraryViewModel(
    private val loadSongsUseCase: LoadSongsUseCase,
    private val addSongToPlaylistUseCase: AddSongToPlaylistUseCase,
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state: StateFlow<LibraryState> = _state.asStateFlow()

    private var cachedSongs: List<Song>? = null

    fun processIntent(intent: LibraryIntent) {
        when (intent) {
            is LibraryIntent.LoadSongs -> {
                loadSongs(forceReload = false)
            }

            is LibraryIntent.RequestPermissionAndLoadSongs -> {
                requestPermissionAndLoadSongs()
            }

            is LibraryIntent.PermissionGranted -> {
                _state.update { it.copy(requestedPermission = null) }
                loadSongs(forceReload = true)
            }

            is LibraryIntent.RefreshSongs -> {
                loadSongs(forceReload = true)
            }

            is LibraryIntent.AddSongToPlaylist -> {
                addSongToPlaylist(
                  intent.playlistId,
                    intent.song
                )
            }

            is LibraryIntent.ShowChoosePlaylistDialog -> {
                _state.value = _state.value.copy(showChoosePlaylistDialog = !_state.value.showChoosePlaylistDialog)
            }

            is LibraryIntent.SongSelected -> {
                _state.update { it.copy(song = intent.song ) }
            }
        }
    }

    fun resetRequestedPermission() {
        _state.update { it.copy(requestedPermission = null) }
    }

    private fun requestPermissionAndLoadSongs() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            loadSongs(forceReload = false)
        } else {
            _state.update { it.copy(requestedPermission = permission) }
        }
    }

    private fun loadSongs(forceReload: Boolean = false) {
        if (!forceReload && cachedSongs != null) {
            _state.update { it.copy(songs = cachedSongs!!, isLoading = false) }
            return
        }

        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, errorMessage = null) }

                // IO
                val rawSongs = withContext(Dispatchers.IO) {
                    loadSongsUseCase()
                }

                val processedSongs = withContext(Dispatchers.Default) {
                    rawSongs.map { song ->
                        song.copy(
                            name = shortenTitle(song.name, 20),
                            artist = shortenTitle(song.artist),
                            duration = formatDuration(song.duration.toLong())
                        )
                    }
                }

                cachedSongs = processedSongs

                _state.update {
                    it.copy(
                        songs = processedSongs,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                Log.d("LibraryViewModel", "Successfully loaded ${processedSongs.size} songs")

            } catch (e: Exception) {
                Log.e("LibraryViewModel", "Error loading songs", e)

                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Không thể tải danh sách bài hát"
                    )
                }
            }
        }
    }

    private fun shortenTitle(title: String, maxLength: Int = 30): String {
        return if (title.length > maxLength) {
            "${title.take(maxLength).trimEnd()}..."
        } else {
            title
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatDuration(durationMs: Long): String {
        val totalSeconds = durationMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun addSongToPlaylist(
        playlistId: Long,
        song: SongEntity,
    ) {
        viewModelScope.launch {
            try {
                addSongToPlaylistUseCase(playlistId, song)
            } catch (e: Exception) {

            }
        }
    }


    fun clearCache() {
        cachedSongs = null
    }

    override fun onCleared() {
        super.onCleared()
        cachedSongs = null
    }
}