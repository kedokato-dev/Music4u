package com.kedokato.music4u.di

import com.kedokato.music4u.data.repository.MusicServiceControllerImpl
import com.kedokato.music4u.domain.usecase.AddPlaylistUseCase
import com.kedokato.music4u.domain.usecase.AddSongToPlaylistUseCase
import com.kedokato.music4u.domain.usecase.ClearUserIdUseCase
import com.kedokato.music4u.domain.usecase.DeletePlaylistUseCase
import com.kedokato.music4u.domain.usecase.DownloadSongUseCase
import com.kedokato.music4u.domain.usecase.LoadSongFromRemoteUseCase
import com.kedokato.music4u.domain.usecase.GetUserIdUseCaseShared
import com.kedokato.music4u.domain.usecase.GetUserProfileUseCase
import com.kedokato.music4u.domain.usecase.LoadDownloadedSongsUseCase
import com.kedokato.music4u.domain.usecase.LoadPlaylistUseCase
import com.kedokato.music4u.domain.usecase.LoadSongFromPlaylistUseCase
import com.kedokato.music4u.domain.usecase.LoadSongsUseCase
import com.kedokato.music4u.domain.usecase.SaveUserIdUseCase
import com.kedokato.music4u.domain.usecase.ScanAndInsertSongsUseCase
import com.kedokato.music4u.domain.usecase.SetUserIdUseCase
import com.kedokato.music4u.domain.usecase.UpdateUserProfileUseCase
import com.kedokato.music4u.domain.usecase.UserAuthenticationUseCase
import com.kedokato.music4u.domain.usecase.UserRegisterUseCase
import com.kedokato.music4u.domain.usecase.home.GetTopAlbumsUseCase
import com.kedokato.music4u.domain.usecase.home.GetTopArtistsUseCase
import com.kedokato.music4u.domain.usecase.home.GetTopTracksUseCase
import com.kedokato.music4u.domain.usecase.music.NextSongUseCase
import com.kedokato.music4u.domain.usecase.music.PauseSongUseCase
import com.kedokato.music4u.domain.usecase.music.PlayPlaylistUseCase
import com.kedokato.music4u.domain.usecase.music.PlaySongFromPlaylistUseCase
import com.kedokato.music4u.domain.usecase.music.PlaySongUseCase
import com.kedokato.music4u.domain.usecase.music.PrevSongUseCase
import com.kedokato.music4u.domain.usecase.music.RepeatSongUseCase
import com.kedokato.music4u.domain.usecase.music.ResumeSongUseCase
import com.kedokato.music4u.domain.usecase.music.ShuffleSongUseCase
import com.kedokato.music4u.domain.usecase.music.StopSongUseCase
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
    single { StopSongUseCase(get()) }
    single { RepeatSongUseCase(get()) }
    single { ShuffleSongUseCase(get()) }
    single { PlayPlaylistUseCase(get()) }
    single { PlaySongFromPlaylistUseCase(get()) }


    // home
    single { GetTopAlbumsUseCase(get()) }
    single { GetTopTracksUseCase(get()) }
    single { GetTopArtistsUseCase(get()) }

}