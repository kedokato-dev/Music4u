package com.kedokato.lession6.di

import com.kedokato.lession6.data.remote.api.GetSongAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

val networkModule = module {

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://static.apero.vn")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(GetSongAPI::class.java)
    }
}
