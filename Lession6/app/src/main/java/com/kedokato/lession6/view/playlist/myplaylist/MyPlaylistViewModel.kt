package com.kedokato.lession6.view.playlist.myplaylist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.database.Entity.PlaylistEntity
import com.kedokato.lession6.usecase.AddPlaylistUseCase
import com.kedokato.lession6.usecase.LoadPlaylistUseCase
import com.kedokato.lession6.usecase.DeletePlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyPlaylistViewModel(
    private val addPlaylistUseCase: AddPlaylistUseCase,
    private val loadPlaylistsUseCase: LoadPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase
) : ViewModel(){
    private val _state = MutableStateFlow(MyPlaylistState())
    val state: StateFlow<MyPlaylistState> = _state.asStateFlow()

    fun processIntent(intent: MyPlaylistIntent) {
        when (intent) {
            is MyPlaylistIntent.OnAddPlaylistClick -> {
                _state.value = _state.value.copy(
                    showDialog = !_state.value.showDialog
                )
            }
            is MyPlaylistIntent.OnPlaylistClick -> {

            }
            is MyPlaylistIntent.OnDeletePlaylist -> {
                deletePlaylist(intent.playlistId)
            }
            is MyPlaylistIntent.OnCreatePlaylist -> {
                createPlaylist(intent.playlistName)
            }
            is MyPlaylistIntent.LoadPlaylists -> {
                loadPlaylists()
            }
            is MyPlaylistIntent.OnMoreOptionClick -> {
            }
        }
    }

    private fun createPlaylist(playlistName: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                withContext(Dispatchers.IO) {
                    addPlaylistUseCase(PlaylistEntity(name = playlistName))
                }

                _state.value = _state.value.copy(
                    showDialog = false,
                    isLoading = false
                )

                loadPlaylists()
            } catch (e: Exception) {
                Log.e("MyPlaylistViewModel", "Error adding playlist", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                val playlists = withContext(Dispatchers.IO) {
                    loadPlaylistsUseCase.invoke()
                }

                _state.value = _state.value.copy(
                    playlists = playlists,
                    isLoading = false,
                    error = null,
                    isEmpty = playlists.isEmpty()
                )
            } catch (e: Exception) {
                Log.e("MyPlaylistViewModel", "Error loading playlists", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                withContext(Dispatchers.IO) {
                    deletePlaylistUseCase(playlistId)
                }

                _state.value = _state.value.copy(isLoading = false)
                loadPlaylists()
            } catch (e: Exception) {
                Log.e("MyPlaylistViewModel", "Error deleting playlist", e)
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}