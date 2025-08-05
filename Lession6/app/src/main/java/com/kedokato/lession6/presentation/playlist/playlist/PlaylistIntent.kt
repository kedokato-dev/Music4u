package com.kedokato.lession6.presentation.playlist.playlist

sealed class PlaylistIntent{
    data class Sort(val isSorting: Boolean) : PlaylistIntent()
    data class DisplayType(val displayType: Boolean) : PlaylistIntent()
    data class DeleteSongInPlaylist(val playlistId: Int) : PlaylistIntent()
    data class LoadSongs(val playlistId: Long) : PlaylistIntent()
}