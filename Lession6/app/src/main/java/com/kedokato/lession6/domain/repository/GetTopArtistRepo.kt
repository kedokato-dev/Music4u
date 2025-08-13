package com.kedokato.lession6.domain.repository

import com.kedokato.lession6.domain.model.ArtistInfo

interface GetTopArtistsRepo {
    suspend fun getTopArtists(): List<ArtistInfo>
}