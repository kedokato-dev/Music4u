package com.kedokato.lession6.di

import com.kedokato.lession6.data.repository.PlaylistRepoImpl
import com.kedokato.lession6.data.repository.SongLocalDataSourceImpl
import com.kedokato.lession6.data.repository.UserRepoImpl
import com.kedokato.lession6.domain.repository.PlaylistRepo
import com.kedokato.lession6.domain.repository.SongLocalDataSource
import com.kedokato.lession6.domain.repository.UserRepo
import org.koin.dsl.module

val repositoryModule = module {
    single<PlaylistRepo> { PlaylistRepoImpl(get()) }
    single<SongLocalDataSource> { SongLocalDataSourceImpl(get()) }
    single<UserRepo> { UserRepoImpl(get(), get()) }
}
