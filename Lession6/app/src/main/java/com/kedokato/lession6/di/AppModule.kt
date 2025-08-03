package com.kedokato.lession6.di

import android.content.ContentResolver
import androidx.room.Room
import com.kedokato.lession6.database.AppDatabase
import com.kedokato.lession6.repoImpl.PlaylistRepoImpl
import com.kedokato.lession6.repoImpl.SongLocalDataSourceImpl
import com.kedokato.lession6.repository.PlaylistRepo
import com.kedokato.lession6.repository.SongLocalDataSource
import com.kedokato.lession6.usecase.AddPlaylistUseCase
import com.kedokato.lession6.usecase.AddSongToPlaylistUseCase
import com.kedokato.lession6.usecase.DeletePlaylistUseCase
import com.kedokato.lession6.usecase.LoadPlaylistUseCase
import com.kedokato.lession6.usecase.LoadSongFromPlaylistUseCase
import com.kedokato.lession6.usecase.LoadSongsUseCase
import com.kedokato.lession6.usecase.ScanAndInsertSongsUseCase
import com.kedokato.lession6.view.library.LibraryViewModel
import com.kedokato.lession6.view.playlist.myplaylist.MyPlaylistViewModel
import com.kedokato.lession6.view.playlist.playlist.PlaylistViewModel
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
    single<SongLocalDataSource> { SongLocalDataSourceImpl(get()) }


    // Use Cases
    single { LoadSongsUseCase(get()) }
    single { ScanAndInsertSongsUseCase(get(), get()) }
    single { AddPlaylistUseCase(get()) }
    single { AddSongToPlaylistUseCase(get()) }
    single { LoadPlaylistUseCase(get()) }
    single { DeletePlaylistUseCase (get()) }
    single { LoadSongFromPlaylistUseCase(get()) }


    // ViewModel
    viewModel { LibraryViewModel(get(), get(), androidContext()) }

    viewModel { PlaylistViewModel(get()) }

    viewModel {
        MyPlaylistViewModel(get(), get(), get())
    }

    single<ContentResolver> { androidContext().contentResolver }

}


