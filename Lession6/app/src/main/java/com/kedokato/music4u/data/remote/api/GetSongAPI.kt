package com.kedokato.music4u.data.remote.api

import com.kedokato.music4u.data.remote.dto.SongDTO
import retrofit2.Call
import retrofit2.http.GET

interface GetSongAPI {
    @GET("techtrek/Remote_audio.json")
     fun getSongFromRemote(): Call<List<SongDTO>>
}