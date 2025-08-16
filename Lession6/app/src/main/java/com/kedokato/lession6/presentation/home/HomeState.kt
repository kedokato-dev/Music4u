package com.kedokato.lession6.presentation.home

import com.kedokato.lession6.domain.model.AlbumInfo
import com.kedokato.lession6.domain.model.ArtistInfo
import com.kedokato.lession6.domain.model.TrackInfo
import com.kedokato.lession6.domain.model.User

data class HomeState(
    val albums: List<AlbumInfo> = emptyList(),
    val tracks: List<TrackInfo> = emptyList(),
    val artists: List<ArtistInfo> = emptyList(),
    val userProfile: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isNetworkError: Boolean = false
)