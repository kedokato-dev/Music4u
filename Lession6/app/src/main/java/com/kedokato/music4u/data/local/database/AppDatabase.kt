package com.kedokato.music4u.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.kedokato.music4u.data.local.database.DAO.PlaylistDao
import com.kedokato.music4u.data.local.database.DAO.UserDAO
import com.kedokato.music4u.data.local.database.Entity.PlaylistEntity
import com.kedokato.music4u.data.local.database.Entity.PlaylistSongCrossRef
import com.kedokato.music4u.data.local.database.Entity.SongEntity
import com.kedokato.music4u.data.local.database.Entity.UserEntity

@Database(
    entities = [
        PlaylistEntity::class,
        SongEntity::class,
        PlaylistSongCrossRef::class,
        UserEntity::class,
    ],
    version = 1,
    exportSchema = true,
//    autoMigrations = [
//        AutoMigration(from = 4, to = 5)
//    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun userDao(): UserDAO
}