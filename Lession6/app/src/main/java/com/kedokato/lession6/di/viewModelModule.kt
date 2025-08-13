package com.kedokato.lession6.di

import com.kedokato.lession6.presentation.home.HomeViewModel
import com.kedokato.lession6.presentation.library.LibraryViewModel
import com.kedokato.lession6.presentation.login.LoginViewModel
import com.kedokato.lession6.presentation.player.PlayerMusicViewModel
import com.kedokato.lession6.presentation.playlist.myplaylist.MyPlaylistViewModel
import com.kedokato.lession6.presentation.playlist.playlist.PlaylistViewModel
import com.kedokato.lession6.presentation.profile.ProfileViewModel
import com.kedokato.lession6.presentation.signup.SignUpViewModel
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
}