package com.kedokato.music4u.presentation.home.top_albums

import com.kedokato.music4u.domain.model.AlbumInfo

data class TopAlbumsDetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isNetworkError: Boolean = false,
    val albums: List<AlbumInfo> = emptyList()
)