package com.kedokato.lession6.di

import com.kedokato.lession6.data.repository.DownloadSongFromRemoteRepoImpl
import com.kedokato.lession6.data.repository.GetSongRepoImpl
import com.kedokato.lession6.data.repository.MusicRepositoryImpl
import com.kedokato.lession6.data.repository.MusicServiceControllerImpl
import com.kedokato.lession6.data.repository.PlaylistRepoImpl
import com.kedokato.lession6.data.repository.SongLocalDataSourceImpl
import com.kedokato.lession6.data.repository.UserPreferenceRepoImpl
import com.kedokato.lession6.data.repository.UserRepoImpl
import com.kedokato.lession6.data.service.MusicServiceController
import com.kedokato.lession6.domain.repository.DownloadSongFromRemoteRepo
import com.kedokato.lession6.domain.repository.GetSongFromRemoteRepo
import com.kedokato.lession6.domain.repository.MusicRepo
import com.kedokato.lession6.domain.repository.PlaylistRepo
import com.kedokato.lession6.domain.repository.SongLocalDataSource
import com.kedokato.lession6.domain.repository.UserPreferenceRepo
import com.kedokato.lession6.domain.repository.UserRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<PlaylistRepo> { PlaylistRepoImpl(get()) }
    single<SongLocalDataSource> { SongLocalDataSourceImpl(get()) }
    single<UserRepo> { UserRepoImpl(get(), get()) }
    single<GetSongFromRemoteRepo> { GetSongRepoImpl(get()) }
    single <DownloadSongFromRemoteRepo> { DownloadSongFromRemoteRepoImpl(androidContext()) }
    single <UserPreferenceRepo> {
        UserPreferenceRepoImpl(
            get(),
        )
    }

    single <MusicRepo>{
        MusicRepositoryImpl(get ())
    }

    single<MusicServiceController> {
        MusicServiceControllerImpl(androidContext())
    }
}
