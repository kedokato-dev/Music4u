package com.kedokato.music4u.data.repository

import com.kedokato.music4u.data.mapper.toArtistInfo
import com.kedokato.music4u.data.remote.api.GetTopArtistsAPI
import com.kedokato.music4u.data.remote.respone.TopArtistsResponse
import com.kedokato.music4u.domain.model.ArtistInfo
import com.kedokato.music4u.domain.repository.GetTopArtistsRepo
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GetTopArtistsRepoImpl(
    private val service: GetTopArtistsAPI
): GetTopArtistsRepo {
    override suspend fun getTopArtists(): List<ArtistInfo> {
        return suspendCancellableCoroutine { cont ->
            val call = service.getTopArtists()
            call.enqueue(object : retrofit2.Callback<TopArtistsResponse> {
                override fun onResponse(
                    call: retrofit2.Call<TopArtistsResponse>,
                    response: retrofit2.Response<TopArtistsResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!
                        val artists = responseBody.artists?.artist ?: emptyList()
                        val artistInfoList = artists.map { it.toArtistInfo() }
                        cont.resume(artistInfoList)
                    } else {
                        cont.resumeWithException(HttpException(response))
                    }
                }

                override fun onFailure(call: retrofit2.Call<TopArtistsResponse>, t: Throwable) {
                    cont.resumeWithException(t)
                }
            })

            cont.invokeOnCancellation {
                call.cancel()
            }
        }
    }
}