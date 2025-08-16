package com.kedokato.lession6.presentation.home.top_albums

import com.kedokato.lession6.domain.model.AlbumInfo

data class TopAlbumsDetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isNetworkError: Boolean = false,
    val albums: List<AlbumInfo> = emptyList()
)