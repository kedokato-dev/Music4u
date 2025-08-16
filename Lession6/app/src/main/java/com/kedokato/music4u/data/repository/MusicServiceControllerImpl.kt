package com.kedokato.music4u.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.kedokato.music4u.data.service.MusicServiceController
import com.kedokato.music4u.domain.model.PlayerState
import com.kedokato.music4u.domain.model.Song
import com.kedokato.music4u.service.MusicService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicServiceControllerImpl(
    private val context: Context
) : MusicServiceController {

    private var musicService: MusicService? = null
    private val _playerState = MutableStateFlow(PlayerState())
    override val playerState: Flow<PlayerState> = _playerState.asStateFlow()

    private var isServiceConnected = false
    private val pendingActions = mutableListOf<() -> Unit>()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            android.util.Log.d("MusicServiceController", "Service connected successfully!")
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isServiceConnected = true

            // Thực hiện các action đang pending
            pendingActions.forEach { it.invoke() }
            pendingActions.clear()

            // **QUAN TRỌNG**: Lắng nghe state từ service
            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                musicService?.playerState?.collect { serviceState ->
                    _playerState.value = serviceState
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            android.util.Log.w("MusicServiceController", "Service disconnected!")
            musicService = null
            isServiceConnected = false
        }
    }

    init {
        bindToService()
    }

    private fun bindToService() {
        android.util.Log.d("MusicServiceController", "Attempting to bind to service...")
        val intent = Intent(context, MusicService::class.java)

        // START service trước khi bind để đảm bảo service tồn tại
        context.startService(intent)
        val bindResult = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        android.util.Log.d("MusicServiceController", "Bind result: $bindResult")
    }

    override suspend fun play(song: Song) {
        android.util.Log.d("MusicServiceController", "Play called for song: ${song.name}")

        if (!isServiceConnected || musicService == null) {
            android.util.Log.w("MusicServiceController", "Service not ready, adding to pending actions")
            // Thêm vào pending actions để thực hiện khi service ready
            pendingActions.add {
                android.util.Log.d("MusicServiceController", "Executing pending play for: ${song.name}")
                musicService?.playSong(song)
            }
            return
        }

        musicService?.playSong(song)
    }

    override suspend fun playPlaylist(
        playlist: List<Song>,
        startIndex: Int
    ) {
        android.util.Log.d("MusicServiceController", "Play playlist called with start index: $startIndex")
        if (!isServiceConnected || musicService == null) {
            android.util.Log.w("MusicServiceController", "Service not ready, adding to pending actions")
            pendingActions.add { musicService?.playPlaylist(playlist, startIndex) }
            return
        }
        musicService?.playPlaylist(playlist, startIndex)
        android.util.Log.d("MusicServiceController", "Play playlist action executed")
    }

    override suspend fun pause() {
        android.util.Log.d("MusicServiceController", "Pause called")
        if (!isServiceConnected || musicService == null) {
            android.util.Log.w("MusicServiceController", "Service not ready, adding pause to pending")
            pendingActions.add { musicService?.pauseSong() }
            return
        }
        musicService?.pauseSong()
    }

    override suspend fun resume() {
        android.util.Log.d("MusicServiceController", "Resume called")
        if (!isServiceConnected || musicService == null) {
            pendingActions.add { musicService?.resumeSong() }
            return
        }
        musicService?.resumeSong()
    }


    override suspend fun seekTo(position: Long) {
        android.util.Log.d("MusicServiceController", "SeekTo called: $position")
        if (!isServiceConnected || musicService == null) {
            pendingActions.add { musicService?.seekTo(position) }
            return
        }
        musicService?.seekTo(position)
    }

    override suspend fun stopSong() {
        android.util.Log.d("MusicServiceController", "Stop called")
        if (!isServiceConnected || musicService == null) {
            musicService?.stopSong()
            return
        }
        musicService?.stopSong()
    }

    override suspend fun skipToNextSong() {
        android.util.Log.d("MusicServiceController", "Skip to next song called")
        if (!isServiceConnected || musicService == null) {
            pendingActions.add { musicService?.skipToNext() }
            return
        }
        musicService?.skipToNext()
    }

    override suspend fun skipToPreviousSong() {
        android.util.Log.d("MusicServiceController", "Skip to previous song called")
        if (!isServiceConnected || musicService == null) {
            pendingActions.add { musicService?.skipToPrevious() }
            return
        }
        musicService?.skipToPrevious()
    }

    override suspend fun playFromPlaylist(index: Int) {
        android.util.Log.d("MusicServiceController", "Play from playlist at index: $index")
        if (!isServiceConnected || musicService == null) {
            pendingActions.add { musicService?.playFromPlaylist(index) }
            return
        }
        musicService?.playFromPlaylist(index)
        android.util.Log.d("MusicServiceController", "Play from playlist action executed")
    }

    override suspend fun toggleRepeatMode() {
        android.util.Log.d("MusicServiceController", "Toggle repeat mode called")
        if (!isServiceConnected || musicService == null) {
            pendingActions.add { musicService?.toggleRepeat() }
            return
        }
        musicService?.toggleRepeat()
    }

    override suspend fun toggleShuffleMode() {
        android.util.Log.d("MusicServiceController", "Toggle shuffle mode called")
        if (!isServiceConnected || musicService == null) {
            pendingActions.add { musicService?.toggleShuffle() }
            return
        }
        musicService?.toggleShuffle()
    }
}