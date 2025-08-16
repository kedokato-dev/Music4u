package com.kedokato.music4u.presentation.playlist.myplaylist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.music4u.data.local.database.Entity.PlaylistEntity
import com.kedokato.music4u.domain.usecase.AddPlaylistUseCase
import com.kedokato.music4u.domain.usecase.DeletePlaylistUseCase
import com.kedokato.music4u.domain.usecase.GetUserIdUseCaseShared
import com.kedokato.music4u.domain.usecase.LoadPlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyPlaylistViewModel(
    private val addPlaylistUseCase: AddPlaylistUseCase,
    private val loadPlaylistsUseCase: LoadPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val getUserIdUseCaseShared: GetUserIdUseCaseShared
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
                    PlaylistEntity(name = playlistName, userId = getUserId())
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
                    loadPlaylistsUseCase(getUserId())
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



    private suspend fun getUserId(): Long {
       return withContext(Dispatchers.IO){
            getUserIdUseCaseShared() ?: 1L
       }
    }
}