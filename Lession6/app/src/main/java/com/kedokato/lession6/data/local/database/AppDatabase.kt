package com.kedokato.lession6.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.kedokato.lession6.data.local.database.DAO.PlaylistDao
import com.kedokato.lession6.data.local.database.DAO.UserDAO
import com.kedokato.lession6.data.local.database.Entity.PlaylistEntity
import com.kedokato.lession6.data.local.database.Entity.PlaylistSongCrossRef
import com.kedokato.lession6.data.local.database.Entity.SongEntity
import com.kedokato.lession6.data.local.database.Entity.UserEntity

@Database(
    entities = [
        PlaylistEntity::class,
        SongEntity::class,
        PlaylistSongCrossRef::class,
        UserEntity::class,
    ],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 4, to = 5)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun userDao(): UserDAO
}