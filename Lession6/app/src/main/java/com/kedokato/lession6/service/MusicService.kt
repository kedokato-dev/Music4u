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


class MusicService : Service() {
    private val binder = MusicBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var currentSong: Song? = null

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: Flow<PlayerState> = _playerState.asStateFlow()

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
            ACTION_PAUSE -> pauseSong()
            ACTION_RESUME -> resumeSong()
            ACTION_SKIP_NEXT -> {}
            ACTION_SKIP_PREVIOUS -> {}
            ACTION_STOP -> {
                stopSelf()
                stopForeground(STOP_FOREGROUND_REMOVE)
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
                    duration = mp.duration.toLong()
                )
                startForeground(NOTIFICATION_ID, createNotification(true))
            }
        }
    }

    fun pauseSong() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            updateNotification(false)
        }
    }

    fun resumeSong() {
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
            updateNotification(true)
        }
    }

    fun seekTo(position: Long) {
        mediaPlayer?.seekTo(position.toInt())
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

    override fun onDestroy() {
        super.onDestroy()
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