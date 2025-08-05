package com.kedokato.lession6.presentation.playlist.myplaylist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.lession6.data.local.database.Entity.PlaylistEntity
import com.kedokato.lession6.domain.usecase.AddPlaylistUseCase
import com.kedokato.lession6.domain.usecase.DeletePlaylistUseCase
import com.kedokato.lession6.domain.usecase.GetUserIdUseCase
import com.kedokato.lession6.domain.usecase.LoadPlaylistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyPlaylistViewModel(
    private val addPlaylistUseCase: AddPlaylistUseCase,
    private val loadPlaylistsUseCase: LoadPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
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
            _state.update { it.copy(isLoading = true) }

            val result = runCatching {
                addPlaylistUseCase(
                    PlaylistEntity(name = playlistName, userId = userid)
                )
            }

            result.onSuccess {
                _state.update {
                    it.copy(showDialog = false, isLoading = false)
                }
                loadPlaylists()
            }.onFailure { e ->
                Log.e("MyPlaylistViewModel", "Error adding playlist", e)
                _state.update {
                    it.copy(isLoading = false, error = e.message ?: "Unexpected error")
                }
            }
        }
    }


    private fun loadPlaylists() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = runCatching {
                    loadPlaylistsUseCase(userid)
            }

            result.onSuccess { playlists ->
                _state.update {
                    it.copy(
                        playlists = playlists,
                        isLoading = false,
                        error = if (playlists.isEmpty()) "No playlists found" else null
                    )
                }
            }.onFailure { e ->
                Log.e("MyPlaylistViewModel", "Error loading playlists", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unexpected error"
                    )
                }
            }
        }
    }


    private fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = runCatching {
                deletePlaylistUseCase(playlistId)
            }

            result.onSuccess {
                _state.update { it.copy(isLoading = false) }
                loadPlaylists()
            }.onFailure { e ->
                Log.e("MyPlaylistViewModel", "Error deleting playlist", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unexpected error"
                    )
                }
            }
        }
    }


    private val userid: Long =  getUserIdUseCase() ?: 1L
}