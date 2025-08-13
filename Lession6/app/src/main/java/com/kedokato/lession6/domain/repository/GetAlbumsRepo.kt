package com.kedokato.lession6.domain.repository

import com.kedokato.lession6.domain.model.AlbumInfo

interface GetAlbumsRepo {
    suspend fun getTopAlbums(): List<AlbumInfo>
}