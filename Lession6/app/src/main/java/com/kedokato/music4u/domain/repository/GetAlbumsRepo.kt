package com.kedokato.music4u.domain.repository

import com.kedokato.music4u.domain.model.AlbumInfo

interface GetAlbumsRepo {
    suspend fun getTopAlbums(): List<AlbumInfo>
}