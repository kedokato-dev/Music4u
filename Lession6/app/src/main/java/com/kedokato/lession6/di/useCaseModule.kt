package com.kedokato.lession6.di

import com.kedokato.lession6.data.repository.MusicServiceControllerImpl
import com.kedokato.lession6.domain.usecase.AddPlaylistUseCase
import com.kedokato.lession6.domain.usecase.AddSongToPlaylistUseCase
import com.kedokato.lession6.domain.usecase.ClearUserIdUseCase
import com.kedokato.lession6.domain.usecase.DeletePlaylistUseCase
import com.kedokato.lession6.domain.usecase.DownloadSongUseCase
import com.kedokato.lession6.domain.usecase.LoadSongFromRemoteUseCase
import com.kedokato.lession6.domain.usecase.GetUserIdUseCaseShared
import com.kedokato.lession6.domain.usecase.GetUserProfileUseCase
import com.kedokato.lession6.domain.usecase.LoadDownloadedSongsUseCase
import com.kedokato.lession6.domain.usecase.LoadPlaylistUseCase
import com.kedokato.lession6.domain.usecase.LoadSongFromPlaylistUseCase
import com.kedokato.lession6.domain.usecase.LoadSongsUseCase
import com.kedokato.lession6.domain.usecase.SaveUserIdUseCase
import com.kedokato.lession6.domain.usecase.ScanAndInsertSongsUseCase
import com.kedokato.lession6.domain.usecase.SetUserIdUseCase
import com.kedokato.lession6.domain.usecase.UpdateUserProfileUseCase
import com.kedokato.lession6.domain.usecase.UserAuthenticationUseCase
import com.kedokato.lession6.domain.usecase.UserRegisterUseCase
import com.kedokato.lession6.domain.usecase.music.NextSongUseCase
import com.kedokato.lession6.domain.usecase.music.PauseSongUseCase
import com.kedokato.lession6.domain.usecase.music.PlaySongUseCase
import com.kedokato.lession6.domain.usecase.music.PrevSongUseCase
import com.kedokato.lession6.domain.usecase.music.ResumeSongUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { LoadSongsUseCase(get()) }
    single { ScanAndInsertSongsUseCase(get(), get()) }
    single { AddPlaylistUseCase(get()) }
    single { AddSongToPlaylistUseCase(get()) }
    single { LoadPlaylistUseCase(get()) }
    single { DeletePlaylistUseCase(get()) }
    single { LoadSongFromPlaylistUseCase(get()) }
    single { UserRegisterUseCase(get()) }
    single { UserAuthenticationUseCase(get()) }
    single { GetUserProfileUseCase(get()) }

    single { SetUserIdUseCase(get()) }
    single { ClearUserIdUseCase(get()) }
    single { UpdateUserProfileUseCase(get()) }

    single { LoadSongFromRemoteUseCase(get()) }
    single { DownloadSongUseCase(get()) }
    single { LoadDownloadedSongsUseCase(get()) }

    single { SaveUserIdUseCase(get()) }
    single { GetUserIdUseCaseShared(get()) }

//    Player Music
    single { MusicServiceControllerImpl(get()) }
    single { PlaySongUseCase(get()) }
    single { PauseSongUseCase(get()) }
    single { NextSongUseCase(get()) }
    single { PrevSongUseCase(get()) }
    single { ResumeSongUseCase(get()) }
}