package com.kedokato.lession6.presentation.home.top_tracks

sealed class TopTracksDetailContract {
    data object LoadTrack : TopTracksDetailContract()
}