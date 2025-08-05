package com.kedokato.lession6.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.SongLocalDataSource

class SongLocalDataSourceImpl(
    private val contentResolver: ContentResolver
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

        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        contentResolver.query(uri, projection, selection, null, sortOrder)?.use { cursor ->
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

                    val imageFile = extractAlbumArtAsBytes( contentResolver, audioUri)

                    Log.d("PlaylistRepo", "Adding song: $title - $artist - $duration")

                    songs.add(
                        Song(
                            id = id,
                            name = title,
                            artist = artist,
                            duration = duration.toString(),
                            image = imageFile,
                            uri = audioUri
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

    private fun extractAlbumArtAsBytes(
        contentResolver: ContentResolver,
        audioUri: Uri
    ): ByteArray? {
        val retriever = MediaMetadataRetriever()
        return try {
            val fd = contentResolver.openFileDescriptor(audioUri, "r")?.fileDescriptor ?: return null
            retriever.setDataSource(fd)
            retriever.embeddedPicture
        } catch (e: Exception) {
            Log.e("PlaylistRepo", "Failed to extract embedded picture: ${e.message}")
            null
        } finally {
            retriever.release()
        }
    }
}
