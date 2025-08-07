package com.kedokato.lession6.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.DownloadSongFromRemoteRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

class DownloadSongFromRemoteRepoImpl(
    private val context: Context,
    private val client: OkHttpClient = OkHttpClient()
) : DownloadSongFromRemoteRepo {

    override suspend fun downloadSongFromRemote(songs: List<Song>, userId: Long): List<Song> {
        return withContext(Dispatchers.IO) {
            songs.map { song ->
                val fileName = generateSafeFileName(song.name, song.artist)

                val userDir = File(context.filesDir, "$userId/songs")
                if (!userDir.exists()) {
                    userDir.mkdirs()
                }

                val file = File(userDir, fileName)
                val isDownloaded = file.exists()

                if (!isDownloaded) {
                    try {
                        val request = Request.Builder().url(song.uri.toString()).build()
                        val response = client.newCall(request).execute()
                        if (response.isSuccessful) {
                            response.body?.byteStream()?.use { input ->
                                FileOutputStream(file).use { output ->
                                    input.copyTo(output)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                val generatedId = (song.name + song.artist).hashCode().toLong()

                Song(
                    id = generatedId,
                    name = song.name,
                    artist = song.artist,
                    duration = song.duration,
                    uri = file.toUri(),
                    image = null
                )
            }
        }
    }

    override suspend fun checkSongExists(
        song: Song,
        userName: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val fileName = generateSafeFileName(song.name, song.artist)
            val userDir = File(context.filesDir, "$userName/songs")
            val file = File(userDir, fileName)
            file.exists()
        }
    }

    override suspend fun getDownloadedSongs(userId: Long): List<Song> {
        return withContext(Dispatchers.IO) {
            val userDir = File(context.filesDir, "$userId/songs")
            if (!userDir.exists()) {
                return@withContext emptyList()
            }

            userDir.listFiles { file -> file.extension.equals("mp3", ignoreCase = true) }
                ?.mapNotNull { file ->
                    try {
                        val fileName = file.nameWithoutExtension
                        val parts = fileName.split("_")
                        if (parts.size >= 2) {
                            val title = parts.dropLast(1).joinToString(" ").replace("_", " ")
                            val artist = parts.last().replace("_", " ")

                            // Extract duration and album art from media file
                            val mediaInfo = extractMediaInfo(file)
                            val generatedId = (title + artist).hashCode().toLong()

                            Song(
                                id = generatedId,
                                name = title,
                                artist = artist,
                                duration = mediaInfo.first,
                                uri = file.toUri(),
                                image = mediaInfo.second
                            )
                        } else null
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()
        }
    }

    private fun extractMediaInfo(file: File): Pair<String, ByteArray?> {
        return try {
            val retriever = android.media.MediaMetadataRetriever()
            retriever.setDataSource(file.absolutePath)

            // Extract duration
            val durationMs = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()
            val duration = durationMs?.let { ms ->
                val seconds = ms / 1000
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                String.format("%02d:%02d", minutes, remainingSeconds)
            } ?: "00:00"

            // Extract album art as ByteArray
            val albumArt = retriever.embeddedPicture

            retriever.release()
            Pair(duration, albumArt)
        } catch (e: Exception) {
            Pair("00:00", null)
        }
    }

    private fun generateSafeFileName(title: String, artist: String): String {
        val raw = "$title-$artist"
        val normalized = raw.lowercase().trim().replace(Regex("[^a-z0-9]+"), "_")
        return "$normalized.mp3"
    }
}
