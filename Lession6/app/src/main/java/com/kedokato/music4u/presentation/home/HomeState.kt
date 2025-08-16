package com.kedokato.music4u.presentation.home

import com.kedokato.music4u.domain.model.AlbumInfo
import com.kedokato.music4u.domain.model.ArtistInfo
import com.kedokato.music4u.domain.model.TrackInfo
import com.kedokato.music4u.domain.model.User

data class HomeState(
    val albums: List<AlbumInfo> = emptyList(),
    val tracks: List<TrackInfo> = emptyList(),
    val artists: List<ArtistInfo> = emptyList(),
    val userProfile: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isNetworkError: Boolean = false
)