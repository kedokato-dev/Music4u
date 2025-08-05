package com.kedokato.lession6.di

import androidx.room.Room
import com.kedokato.lession6.data.local.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "playlist_database"
        )
//            .fallbackToDestructiveMigration() ☠️ ☠️ ☠️
            .build()
    }

    // DAO
    single { get<AppDatabase>().playlistDao() }
    single { get<AppDatabase>().userDao() }
}
