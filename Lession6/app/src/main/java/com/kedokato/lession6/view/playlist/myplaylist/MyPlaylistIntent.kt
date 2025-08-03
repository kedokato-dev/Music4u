package com.kedokato.lession6.view.playlist.myplaylist

sealed class MyPlaylistIntent {
    data object OnAddPlaylistClick : MyPlaylistIntent()
    data class OnPlaylistClick(val playlistId: Long) : MyPlaylistIntent()
    data class OnDeletePlaylist(val playlistId: Long) : MyPlaylistIntent()
    data class OnCreatePlaylist(val playlistName: String) : MyPlaylistIntent()
    data object LoadPlaylists : MyPlaylistIntent()
    data object OnMoreOptionClick : MyPlaylistIntent()
}