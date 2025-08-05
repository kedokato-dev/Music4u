package com.kedokato.lession6.di

import com.kedokato.lession6.domain.usecase.AddPlaylistUseCase
import com.kedokato.lession6.domain.usecase.AddSongToPlaylistUseCase
import com.kedokato.lession6.domain.usecase.ClearUserIdUseCase
import com.kedokato.lession6.domain.usecase.DeletePlaylistUseCase
import com.kedokato.lession6.domain.usecase.GetUserIdUseCase
import com.kedokato.lession6.domain.usecase.GetUserProfileUseCase
import com.kedokato.lession6.domain.usecase.LoadPlaylistUseCase
import com.kedokato.lession6.domain.usecase.LoadSongFromPlaylistUseCase
import com.kedokato.lession6.domain.usecase.LoadSongsUseCase
import com.kedokato.lession6.domain.usecase.ScanAndInsertSongsUseCase
import com.kedokato.lession6.domain.usecase.SetUserIdUseCase
import com.kedokato.lession6.domain.usecase.UpdateUserProfileUseCase
import com.kedokato.lession6.domain.usecase.UserAuthenticationUseCase
import com.kedokato.lession6.domain.usecase.UserRegisterUseCase
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
    single { GetUserIdUseCase(get()) }
    single { SetUserIdUseCase(get()) }
    single { ClearUserIdUseCase(get()) }
    single { UpdateUserProfileUseCase(get()) }
}