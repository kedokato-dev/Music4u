package com.kedokato.lession6.di

import com.kedokato.lession6.data.local.shared_prefer.UserPreferenceDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val dataModule = module {

    single<UserPreferenceDataSource> {
        UserPreferenceDataSource(androidContext())
    }
}
