package com.kedokato.music4u.presentation.home.top_artist

import com.kedokato.music4u.domain.model.ArtistInfo

data class TopArtistDetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isNetworkError: Boolean = false,
    val artists: List<ArtistInfo> = emptyList()
)