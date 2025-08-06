package com.kedokato.lession6.data.repository

import com.kedokato.lession6.data.mapper.SongMapper
import com.kedokato.lession6.data.remote.api.GetSongAPI
import com.kedokato.lession6.data.remote.dto.SongDTO
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.GetSongFromRemoteRepo
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GetSongRepoImpl(
    private val service: GetSongAPI
) : GetSongFromRemoteRepo {
    override suspend fun getSongFromRemote(): List<Song> = suspendCancellableCoroutine { cont ->
        val call = service.getSongFromRemote()
        call.enqueue(object : Callback<List<SongDTO>> {
            override fun onResponse(call: Call<List<SongDTO>>, response: Response<List<SongDTO>>) {
                if (response.isSuccessful && response.body() != null) {
                    val songList = response.body()!!.map { SongMapper.toDomain(it) }
                    cont.resume(songList)
                } else {
                    cont.resumeWithException(HttpException(response))
                }
            }

            override fun onFailure(call: Call<List<SongDTO>>, t: Throwable) {
                cont.resumeWithException(t)
            }
        })

        cont.invokeOnCancellation {
            call.cancel()
        }
    }

}