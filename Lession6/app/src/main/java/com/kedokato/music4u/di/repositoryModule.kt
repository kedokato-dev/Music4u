package com.kedokato.music4u.di

import com.kedokato.music4u.data.repository.DownloadSongFromRemoteRepoImpl
import com.kedokato.music4u.data.repository.GetAlbumsRepoImpl
import com.kedokato.music4u.data.repository.GetSongRepoImpl
import com.kedokato.music4u.data.repository.GetTopArtistsRepoImpl
import com.kedokato.music4u.data.repository.GetTracksRepoImpl
import com.kedokato.music4u.data.repository.MusicRepositoryImpl
import com.kedokato.music4u.data.repository.MusicServiceControllerImpl
import com.kedokato.music4u.data.repository.PlaylistRepoImpl
import com.kedokato.music4u.data.repository.SongLocalDataSourceImpl
import com.kedokato.music4u.data.repository.UserPreferenceRepoImpl
import com.kedokato.music4u.data.repository.UserRepoImpl
import com.kedokato.music4u.data.service.MusicServiceController
import com.kedokato.music4u.domain.repository.DownloadSongFromRemoteRepo
import com.kedokato.music4u.domain.repository.GetAlbumsRepo
import com.kedokato.music4u.domain.repository.GetSongFromRemoteRepo
import com.kedokato.music4u.domain.repository.GetTopArtistsRepo
import com.kedokato.music4u.domain.repository.GetTopTracksRepo
import com.kedokato.music4u.domain.repository.MusicRepo
import com.kedokato.music4u.domain.repository.PlaylistRepo
import com.kedokato.music4u.domain.repository.SongLocalDataSource
import com.kedokato.music4u.domain.repository.UserPreferenceRepo
import com.kedokato.music4u.domain.repository.UserRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<PlaylistRepo> { PlaylistRepoImpl(get()) }
    single<SongLocalDataSource> { SongLocalDataSourceImpl(get(), androidContext()) }
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

    single<GetAlbumsRepo>{
        GetAlbumsRepoImpl(get())
    }

    single<GetTopTracksRepo>{
        GetTracksRepoImpl(get())
    }

    single<GetTopArtistsRepo>{
        GetTopArtistsRepoImpl(get())
    }
}
