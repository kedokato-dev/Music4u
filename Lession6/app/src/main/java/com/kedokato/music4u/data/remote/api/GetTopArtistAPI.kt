package com.kedokato.music4u.data.remote.api

import com.kedokato.music4u.data.remote.respone.TopArtistsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetTopArtistsAPI {
    @GET("2.0/")
    fun getTopArtists(
        @Query("api_key") apiKey: String = "e65449d181214f936368984d4f4d4ae8",
        @Query("format") format: String = "json",
        @Query("method") method: String = "chart.gettopartists"
    ): Call<TopArtistsResponse>
    }
