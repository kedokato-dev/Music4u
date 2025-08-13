package com.kedokato.lession6.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.SongLocalDataSource
import java.io.File
import java.io.FileOutputStream

class SongLocalDataSourceImpl(
    private val contentResolver: ContentResolver,
    private val context: android.content.Context
) : SongLocalDataSource {

    override suspend fun getAllSongs(): List<Song> {
        return getAllMp3Files(contentResolver)
    }

    private fun getAllMp3Files(
        contentResolver: ContentResolver
    ): List<Song> {
        val songs = mutableListOf<Song>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.MIME_TYPE
        )

        contentResolver.query(uri, projection, selection, null, null)?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)

            if (cursor.moveToFirst()) {
                do {
                    val mimeType = cursor.getString(mimeTypeColumn)
                    Log.d("PlaylistRepo", "Found file with mimeType: $mimeType")

                    if (mimeType != null && mimeType != "audio/mpeg") continue


                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn) ?: "Unknown Title"
                    val artist = cursor.getString(artistColumn) ?: "Unknown Artist"
                    val duration = cursor.getLong(durationColumn)
                    val audioUri = ContentUris.withAppendedId(uri, id)

                    val imageUri = extractAlbumArtUri(contentResolver, audioUri)

                    Log.d("PlaylistRepo", "Adding song: $title - $artist - $duration")

                    songs.add(
                        Song(
                            id = id,
                            name = title,
                            artist = artist,
                            duration = duration.toString(),
                            image = imageUri,
                            uri = audioUri.toString()
                        )
                    )

                } while (cursor.moveToNext())
            } else {
                Log.e("PlaylistRepo", "Query returned empty cursor")
            }
        }

        Log.d("PlaylistRepo", "Total songs loaded: ${songs.size}")
        return songs
    }

    private fun extractAlbumArtUri(
        contentResolver: ContentResolver,
        audioUri: Uri
    ): String? {
        val retriever = MediaMetadataRetriever()
        return try {
            val fd = contentResolver.openFileDescriptor(audioUri, "r")?.fileDescriptor ?: return null
            retriever.setDataSource(fd)
            val pictureData = retriever.embeddedPicture ?: return null


            val fileName = "${audioUri.toString().hashCode()}_cover.jpg"
            val coverFile = File(context.cacheDir, fileName)

            if (!coverFile.exists()) {
                FileOutputStream(coverFile).use { it.write(pictureData) }
                Log.d("PlaylistRepo", "Created new cache file: ${coverFile.name}")
            } else {
                Log.d("PlaylistRepo", "Using existing cache file: ${coverFile.name}")
            }

            coverFile.toUri().toString()
        } catch (e: Exception) {
            Log.e("PlaylistRepo", "Failed to extract embedded picture: ${e.message}")
            null
        } finally {
            retriever.release()
        }
    }

}
