package com.kedokato.lession6.di

import android.content.ContentResolver
import androidx.room.Room
import com.kedokato.lession6.database.AppDatabase
import com.kedokato.lession6.repoImpl.PlaylistRepoImpl
import com.kedokato.lession6.repository.PlaylistRepo
import com.kedokato.lession6.usecase.LoadSongsUseCase
import com.kedokato.lession6.view.library.LibraryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "playlist_database"
        ).build()
    }

    // DAO
    single { get<AppDatabase>().playlistDao() }


    // Repositories

    single<PlaylistRepo> { PlaylistRepoImpl(get()) }


    // Use Cases
    single { LoadSongsUseCase(get()) }


    // ViewModel
    viewModel { LibraryViewModel(get(), get () ) }

    single<ContentResolver> { androidContext().contentResolver }

}