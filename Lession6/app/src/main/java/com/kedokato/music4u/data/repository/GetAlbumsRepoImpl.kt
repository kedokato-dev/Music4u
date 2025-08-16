package com.kedokato.music4u.data.repository

import com.kedokato.music4u.data.mapper.toAlbumInfo
import com.kedokato.music4u.data.remote.api.GetTopAlbumsAPI
import com.kedokato.music4u.data.remote.respone.TopAlbumsResponse
import com.kedokato.music4u.domain.model.AlbumInfo
import com.kedokato.music4u.domain.repository.GetAlbumsRepo
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GetAlbumsRepoImpl(
    private val service: GetTopAlbumsAPI
): GetAlbumsRepo {
    override suspend fun getTopAlbums(): List<AlbumInfo> {
        return suspendCancellableCoroutine { cont ->
            val call = service.getTopAlbums(
                mbid = "f9b593e6-4503-414c-99a0-46595ecd2e23"
            )
            call.enqueue(object : retrofit2.Callback<TopAlbumsResponse> {
                override fun onResponse(
                    call: retrofit2.Call<TopAlbumsResponse>,
                    response: retrofit2.Response<TopAlbumsResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val albums = response.body()!!.topalbums.album
                        val albumInfoList = albums.map { it.toAlbumInfo() }
                        cont.resume(albumInfoList)
                    } else {
                        cont.resumeWithException(HttpException(response))
                    }
                }

                override fun onFailure(call: retrofit2.Call<TopAlbumsResponse>, t: Throwable) {
                    cont.resumeWithException(t)
                }
            })

            cont.invokeOnCancellation {
                call.cancel()
            }
        }
    }
}