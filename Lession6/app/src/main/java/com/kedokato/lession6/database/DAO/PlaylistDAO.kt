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
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: SongEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongToPlaylist(crossRef: PlaylistSongCrossRef)

    @Transaction
    @Query("SELECT * FROM playlist")
    fun getAllPlaylists(): Flow<List<PlaylistWithSongs>>
}
