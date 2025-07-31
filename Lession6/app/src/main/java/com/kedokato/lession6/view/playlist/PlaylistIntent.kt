package com.kedokato.lession6.view.playlist

sealed class PlaylistIntent{
    data class Sort(val isSorting: Boolean) : PlaylistIntent()
    data class DisplayType(val displayType: Boolean) : PlaylistIntent()
    data class DeletePlaylist(val playlistId: Int) : PlaylistIntent()
    data object LoadSongs : PlaylistIntent()
}