package com.kedokato.music4u.di

import android.content.ContentResolver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val platformModule = module {
    single<ContentResolver> { androidContext().contentResolver }
}