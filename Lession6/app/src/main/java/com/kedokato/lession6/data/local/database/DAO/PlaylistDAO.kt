package com.kedokato.lession6.data.local.database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kedokato.lession6.data.local.database.Entity.PlaylistEntity
import com.kedokato.lession6.data.local.database.Entity.PlaylistSongCrossRef
import com.kedokato.lession6.data.local.database.Entity.PlaylistWithSongs
import com.kedokato.lession6.data.local.database.Entity.SongEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSongCrossRef(crossRef: PlaylistSongCrossRef)

    @Transaction
    @Query("SELECT * FROM playlist WHERE userId = :userId ")
    suspend fun getPlaylistsWithSongs(userId: Long): List<PlaylistWithSongs>


    @Transaction
    suspend fun deletePlaylistCompletely(playlistId: Long) {
        deletePlaylistSongRelations(playlistId)
        removePlaylist(playlistId)
    }

    @Query("DELETE FROM playlist WHERE playlistId = :playlistId")
    suspend fun removePlaylist(playlistId: Long)

    @Query("DELETE FROM playlist_song WHERE playlistId = :playlistId")
    suspend fun deletePlaylistSongRelations(playlistId: Long)

   @Query("SELECT * FROM songs WHERE songId IN (SELECT songId FROM playlist_song WHERE playlistId = :playlistId)")
    suspend fun loadSongsFromPlaylist(playlistId: Long): List<SongEntity>




}
