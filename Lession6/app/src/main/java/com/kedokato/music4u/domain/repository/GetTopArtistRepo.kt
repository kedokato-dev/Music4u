package com.kedokato.music4u.domain.repository

import com.kedokato.music4u.domain.model.ArtistInfo

interface GetTopArtistsRepo {
    suspend fun getTopArtists(): List<ArtistInfo>
}