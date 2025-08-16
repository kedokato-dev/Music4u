package com.kedokato.music4u.presentation.library

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kedokato.music4u.data.local.database.Entity.SongEntity
import com.kedokato.music4u.domain.model.Song
import com.kedokato.music4u.domain.usecase.AddSongToPlaylistUseCase
import com.kedokato.music4u.domain.usecase.DownloadSongUseCase
import com.kedokato.music4u.domain.usecase.GetUserIdUseCaseShared
import com.kedokato.music4u.domain.usecase.LoadDownloadedSongsUseCase
import com.kedokato.music4u.domain.usecase.LoadSongFromRemoteUseCase
import com.kedokato.music4u.domain.usecase.LoadSongsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class LibraryViewModel(
    private val loadSongsUseCase: LoadSongsUseCase,
    private val addSongToPlaylistUseCase: AddSongToPlaylistUseCase,
    private val loadSongsFromRemoteUseCase: LoadSongFromRemoteUseCase,
    private val downloadSongFromRemoteUseCase: DownloadSongUseCase,
    private val loadDownloadedSongsUseCase: LoadDownloadedSongsUseCase,
    private val getUserIdUseCaseShared: GetUserIdUseCaseShared,
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

            is LibraryIntent.LoadSongsFromRemote -> {
                loadSongsFromRemote()
            }

            is LibraryIntent.RetryLoadSongsFromRemote -> {
                _state.update { it.copy(isLoading = true, errorLoadingFromRemote = null) }
                loadSongsFromRemote()
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
            _state.update { it.copy(localSongs = cachedSongs!!, isLoading = false) }
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
                        localSongs = processedSongs,
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
    private fun loadSongsFromRemote() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, errorMessage = null) }

              // kiem tra xem đã tải bài hát về chưa
                val hasDownloaded = hasDownloadedSongs(getUserIdShared())

                if (hasDownloaded) {
                    //nếu đã tải bài hát về thì lấy từ bộ nhớ trong
                    val downloadedSongs = withContext(Dispatchers.IO) {
                        loadDownloadedSongsUseCase(getUserIdShared())
                    }

                    val processedSongs = withContext(Dispatchers.Default) {
                        downloadedSongs.map { song ->
                            song.copy(
                                name = shortenTitle(song.name, 20),
                                artist = shortenTitle(song.artist),
                                duration = song.duration
                            )
                        }
                    }

                    _state.update {
                        it.copy(
                            remoteSongs = processedSongs,
                            isLoading = false,
                            errorMessage = null,
                        )
                    }

                    Log.d("LibraryViewModel", "Loaded ${processedSongs.size} songs from internal storage")
                } else {
                   // neu chưa tải bài hát về thì lấy từ server
                    val remoteSongs = withContext(Dispatchers.IO) {
                        loadSongsFromRemoteUseCase()
                    }

                    val processedSongs = withContext(Dispatchers.Default) {
                        remoteSongs.map { song ->
                            song.copy(
                                name = shortenTitle(song.name, 20),
                                artist = shortenTitle(song.artist),
                                duration = formatDuration(song.duration.toLong())
                            )
                        }
                    }

                   // tai ve nhung bai hat tu server
                    downloadSongFromRemoteUseCase(processedSongs, getUserIdShared())

                    _state.update {
                        it.copy(
                            remoteSongs = processedSongs,
                            isLoading = false,
                            errorMessage = null,
                            errorLoadingFromRemote = null
                        )
                    }

                    Log.d("LibraryViewModel", "Fetched and downloaded ${processedSongs.size} songs from server")
                }

            } catch (e: IOException) {
                Log.e("LibraryViewModel", "Network error, trying to load downloaded songs", e)
                loadOfflineSongs()
            } catch (e: HttpException) {
                Log.e("LibraryViewModel", "HTTP error, trying to load downloaded songs", e)
                loadOfflineSongs()
            } catch (e: Exception) {
                Log.e("LibraryViewModel", "Unexpected error", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorLoadingFromRemote = "Lỗi không xác định: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadOfflineSongs() {
        viewModelScope.launch {
            try {
                val downloadedSongs = withContext(Dispatchers.IO) {
                    loadDownloadedSongsUseCase(getUserIdShared())
                }

                val processedSongs = withContext(Dispatchers.Default) {
                    downloadedSongs.map { song ->
                        song.copy(
                            name = shortenTitle(song.name, 30),
                            artist = shortenTitle(song.artist)
                        )
                    }
                }

                _state.update {
                    it.copy(
                        remoteSongs = processedSongs,
                        isLoading = false,
                    )
                }

            } catch (e: Exception) {
                Log.e("LibraryViewModel", "Error loading offline songs", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
        }
    }

    private suspend fun hasDownloadedSongs(userId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            val downloadedSongs = loadDownloadedSongsUseCase(userId)
            downloadedSongs.isNotEmpty()
        }
    }





    private suspend fun getUserIdShared() : Long {
       return  withContext(Dispatchers.IO) {
            getUserIdUseCaseShared.invoke() ?: 4L
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