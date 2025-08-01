package com.kedokato.lession6.view.library

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.usecase.LoadSongsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val loadSongsUseCase: LoadSongsUseCase,
    private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state: StateFlow<LibraryState> = _state.asStateFlow()

    fun processIntent(intent: LibraryIntent) {
        when (intent) {
            LibraryIntent.LoadSongs -> {
                loadSongs()
            }

            is LibraryIntent.RequestPermissionAndLoadSongs -> {
                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_AUDIO
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }

                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    processIntent(LibraryIntent.LoadSongs)
                } else {
                    _state.update { it.copy(requestedPermission = permission) }
                }
            }

            is LibraryIntent.PermissionGranted -> {
                processIntent(LibraryIntent.LoadSongs)
            }
        }
    }

    fun resetRequestedPermission() {
        _state.update { it.copy(requestedPermission = null) }
    }

    private fun loadSongs() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val songs = loadSongsUseCase()
                val processed = songs.map {
                    it.copy(
                        name = shortenTitle(it.name, 20),
                        artist = shortenTitle(it.artist),
                        duration = formatDuration(it.duration.toLong())
                    )
                }
                _state.update { it.copy(songs = processed, isLoading = false) }

            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error")
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

    @SuppressLint("DefaultLocale")
    private fun formatDuration(durationMs: Long): String {
        val totalSeconds = durationMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

}
