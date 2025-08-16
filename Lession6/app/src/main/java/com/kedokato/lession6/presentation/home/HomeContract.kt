package com.kedokato.lession6.presentation.home

sealed class HomeIntent {
    object LoadTopAlbums : HomeIntent()
    object LoadTopTracks : HomeIntent()
    object LoadTopArtists : HomeIntent()
    object LoadUserProfile : HomeIntent()
    object RetryLoading : HomeIntent()

}


sealed class HomeEvent{
    data class ShowError(val message: String) : HomeEvent()
    data class ShowLoading(val isLoading: Boolean) : HomeEvent()
    data class ShowTopAlbums(val albums: List<String>) : HomeEvent() // Assuming albums are represented as a list of strings
    data class ShowSongFromRemote(val songs: List<String>) : HomeEvent() // Assuming songs are represented as a list of strings
}