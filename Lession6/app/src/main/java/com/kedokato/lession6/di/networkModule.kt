package com.kedokato.lession6.di

import com.kedokato.lession6.data.remote.api.GetSongAPI
import com.kedokato.lession6.data.remote.api.GetTopAlbumsAPI
import com.kedokato.lession6.data.remote.api.GetTopArtistsAPI
import com.kedokato.lession6.data.remote.api.GetTopTracksAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single<Retrofit>(named("songRetrofit")) {
        Retrofit.Builder()
            .baseUrl("https://static.apero.vn/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single<Retrofit>(qualifier =named("albumRetrofit")) {
        Retrofit.Builder()
            .baseUrl("http://ws.audioscrobbler.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single<GetSongAPI> {
        get<Retrofit>(qualifier = named("songRetrofit"))
            .create(GetSongAPI::class.java)
    }

    single<GetTopAlbumsAPI> {
        get<Retrofit>(qualifier = named("albumRetrofit"))
            .create(GetTopAlbumsAPI::class.java)
    }


    single<GetTopTracksAPI> {
        get<Retrofit>(qualifier = named("albumRetrofit"))
            .create(GetTopTracksAPI::class.java)
    }

    single<GetTopArtistsAPI> {
        get<Retrofit>(qualifier = named("albumRetrofit"))
            .create(GetTopArtistsAPI::class.java)
    }


}
