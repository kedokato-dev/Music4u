package com.kedokato.music4u.presentation.home.top_tracks

sealed class TopTracksDetailContract {
    data object LoadTrack : TopTracksDetailContract()
}