package com.kedokato.lession6.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.net.toUri
import com.kedokato.lession6.MainActivity
import com.kedokato.lession6.R
import com.kedokato.lession6.domain.model.Song
import androidx.media.app.NotificationCompat
import com.kedokato.lession6.domain.model.PlayerState
import com.kedokato.lession6.utils.loadBitmapFromUri
import com.kedokato.lession6.utils.resourceIdToBitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MusicService : Service() {
    private val binder = MusicBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var currentSong: Song? = null

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: Flow<PlayerState> = _playerState.asStateFlow()


    private var progressUpdateJob: kotlinx.coroutines.Job? = null
    private val serviceScope = kotlinx.coroutines.CoroutineScope(
        kotlinx.coroutines.Dispatchers.Main + kotlinx.coroutines.SupervisorJob()
    )


    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "MusicPlayerChannel"
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val ACTION_SKIP_NEXT = "ACTION_SKIP_NEXT"
        const val ACTION_SKIP_PREVIOUS = "ACTION_SKIP_PREVIOUS"
        const val ACTION_STOP = "ACTION_STOP"
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {

            }
            ACTION_PAUSE -> pauseSong()
            ACTION_RESUME -> resumeSong()
            ACTION_SKIP_NEXT -> {}
            ACTION_SKIP_PREVIOUS -> {}
            ACTION_STOP -> {
               stopSong()
            }
        }
        return START_NOT_STICKY
    }




    fun playSong(song: Song) {
        if (currentSong?.id == song.id && mediaPlayer?.isPlaying == true) return

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            song.uri?.toUri()?.let { setDataSource(this@MusicService, it) }
            prepareAsync()
            setOnPreparedListener { mp ->
                mp.start()
                currentSong = song
                _playerState.value = PlayerState(
                    song = song,
                    isPlaying = true,
                    currentPosition = 0,
                    duration = mp.duration.toLong(),
                    progress = 0f
                )
                startForeground(NOTIFICATION_ID, createNotification(true))
                startProgressUpdates()
            }
        }
    }

    fun pauseSong() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            _playerState.value = _playerState.value.copy(isPlaying = false)
            updateNotification(false)
            stopProgressUpdates()
        }
    }

    fun resumeSong() {
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
            _playerState.value = _playerState.value.copy(isPlaying = true)
            updateNotification(true)
            startProgressUpdates()
        }
    }

    fun seekTo(position: Long) {
        mediaPlayer?.seekTo(position.toInt())
        val duration = mediaPlayer?.duration?.toLong() ?: 0L
        val progress = if (duration > 0) position.toFloat() / duration.toFloat() else 0f

        _playerState.value = _playerState.value.copy(
            currentPosition = position,
            progress = progress
        )
    }

    fun stopSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        currentSong = null
        _playerState.value = PlayerState()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopProgressUpdates()
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0

    fun getDuration(): Int = mediaPlayer?.duration ?: 0


    private fun createNotification(isPlaying: Boolean): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Thông báo điều khiển trình phát nhạc"
            }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val playPauseIcon =
            if (isPlaying) R.drawable.pause_circle else R.drawable.play_circle
        val playPauseAction = if (isPlaying) ACTION_PAUSE else ACTION_RESUME

        val songTitle = currentSong?.name ?: "Unknow title"
        val songArtist = currentSong?.artist ?: "Unknown artist"
        val songImage: Uri = currentSong?.image?.toUri() ?: Uri.EMPTY
        val thumbnailBitmap = loadBitmapFromUri(this, songImage)


        val builder = androidx.core.app.NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_app)
            .setContentTitle(songTitle)
            .setContentText(songArtist)
            .setContentIntent(pendingIntent)
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_LOW)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(null)
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(getPendingIntent(ACTION_STOP))
            )
            .addAction(
                R.drawable.skip_previous,
                "Previous",
                getPendingIntent(ACTION_SKIP_PREVIOUS)
            )
            .addAction(
                playPauseIcon,
                if (isPlaying) "Pause" else "Resume",
                getPendingIntent(playPauseAction)
            )
            .addAction(R.drawable.skip_next, "Next", getPendingIntent(ACTION_SKIP_NEXT))
            .addAction(R.drawable.close, "Close", getPendingIntent(ACTION_STOP))
            .setDeleteIntent(getPendingIntent(ACTION_STOP))
        if (thumbnailBitmap != null) {
            builder.setLargeIcon(thumbnailBitmap)
        }else{
            builder.setLargeIcon(resourceIdToBitmap(this, R.drawable.apple_music))
        }

        return builder.build()
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun updateNotification(isPlaying: Boolean) {
        val notification = createNotification(isPlaying)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun startProgressUpdates() {
        stopProgressUpdates() // Dừng job cũ nếu có

        progressUpdateJob = serviceScope.launch {
            while (mediaPlayer?.isPlaying == true) {
                val currentPos = mediaPlayer?.currentPosition?.toLong() ?: 0L
                val duration = mediaPlayer?.duration?.toLong() ?: 0L
                val progress = if (duration > 0) currentPos.toFloat() / duration.toFloat() else 0f

                _playerState.value = _playerState.value.copy(
                    currentPosition = currentPos,
                    duration = duration,
                    progress = progress
                )

                kotlinx.coroutines.delay(500) // Update mỗi 100ms
            }
        }
    }

    private fun stopProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopProgressUpdates()
        mediaPlayer?.release()
        mediaPlayer = null
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        // Giữ service chạy ngay cả khi app bị remove khỏi recent tasks
        val restartIntent = Intent(applicationContext, MusicService::class.java)
        startService(restartIntent)
    }

}