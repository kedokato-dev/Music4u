package com.kedokato.lession6.di

import com.kedokato.lession6.data.session.SessionManager
import org.koin.dsl.module

val sessionModule = module {
    single { SessionManager() }
}
