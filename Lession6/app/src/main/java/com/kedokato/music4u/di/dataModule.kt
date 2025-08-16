package com.kedokato.music4u.di

import com.kedokato.music4u.data.local.shared_prefer.UserPreferenceDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val dataModule = module {

    single<UserPreferenceDataSource> {
        UserPreferenceDataSource(androidContext())
    }
}
