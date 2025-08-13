package com.kedokato.lession6.domain.repository

import com.kedokato.lession6.domain.model.TrackInfo


interface GetTopTracksRepo {
    suspend fun getTopTracks(): List<TrackInfo>
}