package com.kedokato.lession6.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.kedokato.lession6.domain.model.PlayerState
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.utils.NotificationHelper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class MusicService : Service() {
    companion object {
        const val ACTION_PLAY_PAUSE = "action_play_pause"
        const val ACTION_NEXT = "action_next"
        const val ACTION_PREVIOUS = "action_previous"
        const val ACTION_CLOSE = "action_close"
        private const val TAG = "MusicService"
    }

    private var currentSongId: Long? = null
    private var progressUpdateJob: Job? = null

    // Thêm CoroutineExceptionHandler để xử lý lỗi
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        android.util.Log.e(TAG, "Coroutine exception: ${throwable.message}", throwable)
    }

    private val serviceScope = CoroutineScope(
        Dispatchers.Main + SupervisorJob() + exceptionHandler
    )

    private val binder = LocalBinder()
    private var exoPlayer: ExoPlayer? = null
    private var mediaSession: MediaSession? = null
    private var isForegroundStarted = false
    private var isDestroyed = false

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()
        android.util.Log.d(TAG, "Service onCreate called")
        isDestroyed = false

        try {
            // Tạo notification channel
            NotificationHelper.createNotificationChannel(this)

            // Khởi tạo ExoPlayer với xử lý lỗi
            initializePlayer()

        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error in onCreate", e)
            stopSelf()
        }
    }

    private fun initializePlayer() {
        try {
            exoPlayer?.release()

            exoPlayer = ExoPlayer.Builder(this)
                .setWakeMode(C.WAKE_MODE_NETWORK)
                .setHandleAudioBecomingNoisy(true)
                .build()

            exoPlayer?.let { player ->
                // Tạo MediaSession
                mediaSession?.release()
                mediaSession = MediaSession.Builder(this, player).build()
                setupPlayerListeners()
            }
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error initializing player", e)
            throw e
        }
    }

    private fun setupPlayerListeners() {
        exoPlayer?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isDestroyed) return

                try {
                    android.util.Log.d(TAG, "Playing state changed: $isPlaying")
                    _playerState.value = _playerState.value.copy(isPlaying = isPlaying)

                    // Cập nhật notification
                    updateNotification()

                    if (isPlaying) {
                        startProgressUpdate()
                    } else {
                        updateCurrentProgress()
                        stopProgressUpdate()
                    }
                } catch (e: Exception) {
                    android.util.Log.e(TAG, "Error in onIsPlayingChanged", e)
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (isDestroyed) return

                try {
                    android.util.Log.d(TAG, "Playback state changed: $playbackState")
                    when (playbackState) {
                        Player.STATE_READY -> {
                            updateDuration()
                            updateNotification()
                        }
                        Player.STATE_ENDED -> {
                            _playerState.value = _playerState.value.copy(isPlaying = false)
                            updateNotification()
                        }
                        Player.STATE_IDLE -> {
                            // Xử lý khi player bị idle
                            android.util.Log.w(TAG, "Player is in IDLE state")
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e(TAG, "Error in onPlaybackStateChanged", e)
                }
            }

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                super.onPlayerError(error)
                android.util.Log.e(TAG, "Player error: ${error.message}", error)

                try {
                    _playerState.value = _playerState.value.copy(isPlaying = false)
                    updateNotification()

                    // Thử khôi phục player
                    serviceScope.launch {
                        delay(1000)
                        if (!isDestroyed) {
                            initializePlayer()
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e(TAG, "Error handling player error", e)
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                if (isDestroyed) return

                try {
                    updateDurationWhenReady()
                    updateNotification()
                } catch (e: Exception) {
                    android.util.Log.e(TAG, "Error in onMediaItemTransition", e)
                }
            }
        })
    }

    private fun startForegroundService() {
        if (!isForegroundStarted && !isDestroyed) {
            try {
                val notification = NotificationHelper.createMusicNotification(
                    this,
                    _playerState.value.currentSong,
                    _playerState.value.isPlaying,
                    mediaSession
                ).build()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startForeground(
                        NotificationHelper.NOTIFICATION_ID_CONST,
                        notification,
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                    )
                } else {
                    startForeground(NotificationHelper.NOTIFICATION_ID_CONST, notification)
                }
                isForegroundStarted = true
                android.util.Log.d(TAG, "Started foreground service")
            } catch (e: Exception) {
                android.util.Log.e(TAG, "Error starting foreground service", e)
            }
        }
    }

    private fun updateNotification() {
        if (isForegroundStarted && !isDestroyed) {
            try {
                val notification = NotificationHelper.createMusicNotification(
                    this,
                    _playerState.value.currentSong,
                    _playerState.value.isPlaying,
                    mediaSession
                ).build()

                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
                notificationManager.notify(NotificationHelper.NOTIFICATION_ID_CONST, notification)
            } catch (e: Exception) {
                android.util.Log.e(TAG, "Error updating notification", e)
            }
        }
    }

    private fun stopForegroundService() {
        try {
            if (isForegroundStarted) {
                stopForeground(STOP_FOREGROUND_REMOVE)
                isForegroundStarted = false
            }
            exoPlayer?.stop()
            exoPlayer?.clearMediaItems()
            _playerState.value = PlayerState()
            stopSelf()
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error stopping foreground service", e)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            when (intent?.action) {
                ACTION_PLAY_PAUSE -> {
                    if (_playerState.value.isPlaying) {
                        pause()
                    } else {
                        resume()
                    }
                }
                ACTION_NEXT -> next()
                ACTION_PREVIOUS -> prev()
                ACTION_CLOSE -> {
                    stopForegroundService()
                    return START_NOT_STICKY
                }
            }
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error in onStartCommand", e)
        }
        return START_STICKY
    }

    fun play(song: Song) {
        android.util.Log.d(TAG, "Play function called for song: ${song.name}")

        if (isDestroyed) {
            android.util.Log.w(TAG, "Service is destroyed, cannot play")
            return
        }

        val uri = song.uri
        if (uri.isNullOrEmpty()) {
            android.util.Log.e(TAG, "URI is null or empty for song: ${song.name}")
            return
        }

        try {
            val player = exoPlayer ?: return

            if (currentSongId != song.id) {
                val mediaItem = MediaItem.fromUri(uri.toUri())
                player.clearMediaItems()
                player.setMediaItem(mediaItem)
                player.prepare()
                currentSongId = song.id
            }

            _playerState.value = _playerState.value.copy(
                currentSong = song,
                isPlaying = true,
                currentPosition = player.currentPosition
            )

            player.play()
            startForegroundService()

        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error playing song: ${song.name}", e)
        }
    }

    fun pause() {
        try {
            exoPlayer?.pause()
            _playerState.value = _playerState.value.copy(isPlaying = false)
            android.util.Log.d(TAG, "Paused playback")
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error pausing", e)
        }
    }

    fun resume() {
        android.util.Log.d(TAG, "Resuming playback")
        try {
            if (_playerState.value.currentSong != null && exoPlayer != null) {
                exoPlayer?.play()
                _playerState.value = _playerState.value.copy(isPlaying = true)
                if (!isForegroundStarted) {
                    startForegroundService()
                }
            }
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error resuming", e)
        }
    }

    fun next() {
        android.util.Log.d(TAG, "Next song requested")
    }

    fun prev() {
        android.util.Log.d(TAG, "Previous song requested")
    }

    fun seekTo(position: Long) {
        android.util.Log.d(TAG, "SeekTo: $position")
        try {
            val player = exoPlayer ?: return
            if (player.playbackState == Player.STATE_IDLE) return

            player.seekTo(position)
            updateCurrentProgress()
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error seeking", e)
        }
    }

    private fun startProgressUpdate() {
        progressUpdateJob?.cancel()
        progressUpdateJob = serviceScope.launch {
            try {
                while (isActive && !isDestroyed && exoPlayer?.isPlaying == true) {
                    updateCurrentProgress()
                    delay(500) // Giảm tần suất cập nhật để tối ưu hiệu năng
                }
            } catch (e: Exception) {
                android.util.Log.e(TAG, "Error in progress update", e)
            }
        }
    }

    private fun stopProgressUpdate() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }

    private fun updateCurrentProgress() {
        try {
            val player = exoPlayer ?: return
            val currentPosition = player.currentPosition
            val duration = player.duration

            if (duration != C.TIME_UNSET && duration > 0) {
                val progress = currentPosition.toFloat() / duration.toFloat()
                _playerState.value = _playerState.value.copy(
                    currentPosition = currentPosition,
                    duration = duration,
                    progress = progress
                )
            }
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error updating progress", e)
        }
    }

    private fun updateDuration() {
        try {
            val duration = exoPlayer?.duration ?: return
            if (duration != C.TIME_UNSET) {
                _playerState.value = _playerState.value.copy(duration = duration)
            }
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error updating duration", e)
        }
    }

    private fun updateDurationWhenReady() {
        serviceScope.launch {
            try {
                delay(100)
                if (!isDestroyed) {
                    updateDuration()
                }
            } catch (e: Exception) {
                android.util.Log.e(TAG, "Error in updateDurationWhenReady", e)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        android.util.Log.d(TAG, "Service onBind called")
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        android.util.Log.d(TAG, "Service onDestroy called")

        isDestroyed = true

        try {
            stopProgressUpdate()
            serviceScope.cancel()
            mediaSession?.release()
            mediaSession = null
            exoPlayer?.release()
            exoPlayer = null
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error in onDestroy", e)
        }
    }

    override fun onTaskRemoved(intent: Intent?) {
        super.onTaskRemoved(intent)
        android.util.Log.d(TAG, "Task removed - keeping service alive")

        try {
            // Chỉ dừng service nếu không đang phát nhạc
            if (!_playerState.value.isPlaying) {
                stopSelf()
            }
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error in onTaskRemoved", e)
        }
    }
}