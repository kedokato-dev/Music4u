package com.kedokato.lession6.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.kedokato.lession6.data.service.MusicServiceController
import com.kedokato.lession6.domain.model.PlayerState
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.service.MusicService
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

    // Thêm này để đợi service connection
    private var isServiceConnected = false
    private val pendingActions = mutableListOf<() -> Unit>()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            android.util.Log.d("MusicServiceController", "Service connected successfully!")
            val binder = service as MusicService.LocalBinder
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
                musicService?.play(song)
            }
            return
        }

        musicService?.play(song)
    }

    override suspend fun pause() {
        android.util.Log.d("MusicServiceController", "Pause called")
        if (!isServiceConnected || musicService == null) {
            android.util.Log.w("MusicServiceController", "Service not ready, adding pause to pending")
            pendingActions.add { musicService?.pause() }
            return
        }
        musicService?.pause()
    }

    override suspend fun resume() {
        android.util.Log.d("MusicServiceController", "Resume called")
        if (!isServiceConnected || musicService == null) {
            pendingActions.add { musicService?.resume() }
            return
        }
        musicService?.resume()
    }

    override suspend fun next() {
        android.util.Log.d("MusicServiceController", "Next called")
        if (!isServiceConnected || musicService == null) {
            pendingActions.add { musicService?.next() }
            return
        }
        musicService?.next()
    }

    override suspend fun prev() {
        android.util.Log.d("MusicServiceController", "Previous called")
        if (!isServiceConnected || musicService == null) {
            pendingActions.add { musicService?.prev() }
            return
        }
        musicService?.prev()
    }

    override suspend fun seekTo(position: Long) {
        android.util.Log.d("MusicServiceController", "SeekTo called: $position")
        if (!isServiceConnected || musicService == null) {
            pendingActions.add { musicService?.seekTo(position) }
            return
        }
        musicService?.seekTo(position)
    }
}