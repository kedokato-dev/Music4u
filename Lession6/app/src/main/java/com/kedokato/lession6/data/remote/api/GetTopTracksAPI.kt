package com.kedokato.lession6.data.remote.api

import com.kedokato.lession6.data.remote.respone.TopTracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetTopTracksAPI {
    @GET("2.0/")
    fun getTopTracks(
        @Query("api_key") apiKey: String = "e65449d181214f936368984d4f4d4ae8",
        @Query("format") format: String = "json",
        @Query("method") method: String = "artist.getTopTracks",
        @Query("mbid") mbid: String
    ): Call<TopTracksResponse>
}