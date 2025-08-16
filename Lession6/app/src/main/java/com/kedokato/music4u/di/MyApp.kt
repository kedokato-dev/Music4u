package com.kedokato.music4u.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        GlobalContext.startKoin {
            androidContext(this@MyApp)
            modules(
                databaseModule,
                dataModule,
                sessionModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
                platformModule,
                networkModule,
            )
        }

    }
}