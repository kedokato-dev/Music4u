package com.kedokato.lession6.database.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kedokato.lession6.database.Entity.PlaylistEntity
import com.kedokato.lession6.database.Entity.PlaylistSongCrossRef
import com.kedokato.lession6.database.Entity.PlaylistWithSongs
import com.kedokato.lession6.database.Entity.SongEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSongCrossRef(crossRef: PlaylistSongCrossRef)

    @Transaction
    @Query("SELECT * FROM playlist")
    suspend fun getPlaylistsWithSongs(): List<PlaylistWithSongs>


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
