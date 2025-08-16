package com.kedokato.music4u.di

import com.kedokato.music4u.data.session.SessionManager
import org.koin.dsl.module

val sessionModule = module {
    single { SessionManager() }
}
