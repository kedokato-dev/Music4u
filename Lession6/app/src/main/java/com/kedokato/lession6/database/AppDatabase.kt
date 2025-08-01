package com.kedokato.lession6.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kedokato.lession6.database.DAO.PlaylistDao
import com.kedokato.lession6.database.Entity.PlaylistEntity
import com.kedokato.lession6.database.Entity.PlaylistSongCrossRef
import com.kedokato.lession6.database.Entity.SongEntity

@Database(
    entities = [
        PlaylistEntity::class,
        SongEntity::class,
        PlaylistSongCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}
