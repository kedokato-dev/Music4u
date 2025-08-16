package com.kedokato.music4u.presentation.home.top_artist


sealed class TopArtistDetailContract {
    data object LoadTopArtists : TopArtistDetailContract()
}