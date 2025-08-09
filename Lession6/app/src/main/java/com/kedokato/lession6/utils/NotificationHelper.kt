package com.kedokato.lession6.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import com.kedokato.lession6.R
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.service.MusicService

@UnstableApi
object NotificationHelper {
    private const val NOTIFICATION_ID = 1
    private const val CHANNEL_ID = "music_playback_channel"
    private const val CHANNEL_NAME = "Music Playback"
    private const val TAG = "NotificationHelper"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Controls for music playback"
                    setShowBadge(false)
                    setSound(null, null)
                }

                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                notificationManager?.createNotificationChannel(channel)
            } catch (e: Exception) {
                android.util.Log.e(TAG, "Error creating notification channel", e)
            }
        }
    }

    fun createMusicNotification(
        context: Context,
        song: Song?,
        isPlaying: Boolean,
        mediaSession: MediaSession? = null
    ): NotificationCompat.Builder {

        return try {
            // Intent cho khi click vào notification
            val contentIntent = createContentIntent(context)

            // Action intents
            val prevIntent = createActionIntent(context, MusicService.ACTION_PREVIOUS)
            val playPauseIntent = createActionIntent(context, MusicService.ACTION_PLAY_PAUSE)
            val nextIntent = createActionIntent(context, MusicService.ACTION_NEXT)
            val closeIntent = createActionIntent(context, MusicService.ACTION_CLOSE)

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(song?.name?.take(50) ?: "Unknown Song") // Giới hạn độ dài title
                .setContentText(song?.artist?.take(50) ?: "Unknown Artist") // Giới hạn độ dài artist
                .setLargeIcon(getAlbumArt(context, song))
                .setContentIntent(contentIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW) // Đặt priority thấp để tối ưu
                .addAction(
                    android.R.drawable.ic_media_previous,
                    "Previous",
                    prevIntent
                )
                .addAction(
                    if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
                    if (isPlaying) "Pause" else "Play",
                    playPauseIntent
                )
                .addAction(
                    android.R.drawable.ic_media_next,
                    "Next",
                    nextIntent
                )
                .addAction(
                    android.R.drawable.ic_delete,
                    "Close",
                    closeIntent
                )
                .setDeleteIntent(closeIntent)

            // Chỉ thêm MediaStyle nếu có MediaSession
            mediaSession?.let { session ->
                try {
                    notification.setStyle(
                        androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setShowCancelButton(true)
                            .setCancelButtonIntent(closeIntent)
//                            .setMediaSession(session.)
                    )
                } catch (e: Exception) {
                    android.util.Log.w(TAG, "Cannot set MediaStyle: ${e.message}")
                }
            }

            notification

        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error creating notification", e)
            // Trả về notification đơn giản nếu có lỗi
            createSimpleNotification(context, song, isPlaying)
        }
    }

    private fun createContentIntent(context: Context): PendingIntent {
        return try {
            PendingIntent.getActivity(
                context,
                0,
                context.packageManager.getLaunchIntentForPackage(context.packageName),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error creating content intent", e)
            // Intent rỗng nếu có lỗi
            PendingIntent.getActivity(
                context,
                0,
                Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    private fun createActionIntent(context: Context, action: String): PendingIntent {
        return try {
            val intent = Intent(context, MusicService::class.java).apply {
                this.action = action
            }
            PendingIntent.getService(
                context,
                action.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error creating action intent for $action", e)
            // Intent rỗng nếu có lỗi
            PendingIntent.getService(
                context,
                0,
                Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    private fun getAlbumArt(context: Context, song: Song?): Bitmap? {
        return try {
            // Tạo bitmap nhỏ để tránh OutOfMemoryError
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = false
                inSampleSize = 4 // Giảm kích thước bitmap
                inPreferredConfig = Bitmap.Config.RGB_565 // Sử dụng format nhẹ hơn
            }

            BitmapFactory.decodeResource(context.resources, android.R.drawable.ic_media_play, options)

        } catch (e: OutOfMemoryError) {
            android.util.Log.e(TAG, "OutOfMemoryError when creating album art", e)
            null
        } catch (e: Exception) {
            android.util.Log.e(TAG, "Error creating album art", e)
            null
        }
    }

    private fun createSimpleNotification(
        context: Context,
        song: Song?,
        isPlaying: Boolean
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle(song?.name ?: "Music Player")
            .setContentText(if (isPlaying) "Playing" else "Paused")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
    }

    const val NOTIFICATION_ID_CONST = NOTIFICATION_ID
}