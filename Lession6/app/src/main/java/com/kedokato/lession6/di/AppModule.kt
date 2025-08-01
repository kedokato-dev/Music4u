package com.kedokato.lession6.di

import androidx.room.Room
import com.kedokato.lession6.database.AppDatabase
import org.koin.dsl.module

val appModule = module {

//     Room Database
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "playlist_database"
        ).build()
    }

    // DAO
    single { get<AppDatabase>().playlistDao() }

//    // Repository
//    single { PlaylistRepository(get()) }
//
//    // ViewModel
//    single { PlaylistViewModel(get()) }
}