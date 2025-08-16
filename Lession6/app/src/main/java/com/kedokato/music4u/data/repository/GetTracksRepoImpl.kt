package com.kedokato.music4u.data.repository

import com.kedokato.music4u.data.mapper.toTrackInfo
import com.kedokato.music4u.data.remote.api.GetTopTracksAPI
import com.kedokato.music4u.data.remote.respone.TopTracksResponse
import com.kedokato.music4u.domain.model.TrackInfo
import com.kedokato.music4u.domain.repository.GetTopTracksRepo
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GetTracksRepoImpl(
    private val service: GetTopTracksAPI
): GetTopTracksRepo {
    override suspend fun getTopTracks(): List<TrackInfo> {
        return suspendCancellableCoroutine { cont ->
            val call = service.getTopTracks(
                mbid = "f9b593e6-4503-414c-99a0-46595ecd2e23"
            )
            call.enqueue(object : retrofit2.Callback<TopTracksResponse> {
                override fun onResponse(
                    call: retrofit2.Call<TopTracksResponse>,
                    response: retrofit2.Response<TopTracksResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val tracks = response.body()!!.toptracks.track
                        val trackInfoList = tracks.map { it.toTrackInfo() }
                        cont.resume(trackInfoList)
                    } else {
                        cont.resumeWithException(HttpException(response))
                    }
                }

                override fun onFailure(call: retrofit2.Call<TopTracksResponse>, t: Throwable) {
                    cont.resumeWithException(t)
                }
            })

            cont.invokeOnCancellation {
                call.cancel()
            }
        }

    }
}