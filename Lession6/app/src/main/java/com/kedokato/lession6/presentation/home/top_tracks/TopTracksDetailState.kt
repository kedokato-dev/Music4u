package com.kedokato.lession6.presentation.home.top_tracks

import com.kedokato.lession6.domain.model.TrackInfo

data class TopTracksDetailState(
    val isLoading: Boolean = false,
    val isNetworkError: Boolean = false,
    val tracks: List<TrackInfo> = emptyList(),
    val error: String? = null
)
