package com.kedokato.lession6.presentation.home.top_artist

import com.kedokato.lession6.domain.model.ArtistInfo

data class TopArtistDetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isNetworkError: Boolean = false,
    val artists: List<ArtistInfo> = emptyList()
)