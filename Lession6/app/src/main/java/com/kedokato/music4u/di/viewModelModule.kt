package com.kedokato.music4u.di

import com.kedokato.music4u.presentation.home.HomeViewModel
import com.kedokato.music4u.presentation.home.top_albums.TopAlbumsViewModel
import com.kedokato.music4u.presentation.home.top_tracks.TopTracksViewModel
import com.kedokato.music4u.presentation.library.LibraryViewModel
import com.kedokato.music4u.presentation.login.LoginViewModel
import com.kedokato.music4u.presentation.player.PlayerMusicViewModel
import com.kedokato.music4u.presentation.playlist.myplaylist.MyPlaylistViewModel
import com.kedokato.music4u.presentation.playlist.playlist.PlaylistViewModel
import com.kedokato.music4u.presentation.profile.ProfileViewModel
import com.kedokato.music4u.presentation.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

val viewModelModule = module {
    viewModel { SignUpViewModel(get()) }

    viewModel { LibraryViewModel(
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        androidContext())
    }

    viewModel {
        PlaylistViewModel(
            get(),
            get(),
            get(),
            get(),
            get (),
            get(),
            get()
        )
    }

    viewModel { MyPlaylistViewModel(
        get(),
        get(),
        get(),
        get()) }

    viewModel { LoginViewModel(get(),
        get(),
        get(),
    get()) }

    viewModel {
        ProfileViewModel(
            get(),
            get(),
            get()
        )
    }

    viewModel { HomeViewModel(get(),
        get(),
        get(),
        get(),
        get()
    ) }

    viewModel{
        PlayerMusicViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }

    // Top Albums ViewModel
    viewModel{
        TopAlbumsViewModel(
            get(),
            get()
        )
    }

    viewModel{
        TopTracksViewModel(
            get(),
            get()
        )
    }
}