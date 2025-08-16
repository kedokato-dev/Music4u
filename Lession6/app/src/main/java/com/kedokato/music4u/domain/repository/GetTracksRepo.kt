package com.kedokato.music4u.domain.repository

import com.kedokato.music4u.domain.model.TrackInfo


interface GetTopTracksRepo {
    suspend fun getTopTracks(): List<TrackInfo>
}