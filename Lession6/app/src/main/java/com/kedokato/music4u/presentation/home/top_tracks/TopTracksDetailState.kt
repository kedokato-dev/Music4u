package com.kedokato.music4u.presentation.home.top_tracks

import com.kedokato.music4u.domain.model.TrackInfo

data class TopTracksDetailState(
    val isLoading: Boolean = false,
    val isNetworkError: Boolean = false,
    val tracks: List<TrackInfo> = emptyList(),
    val error: String? = null
)
