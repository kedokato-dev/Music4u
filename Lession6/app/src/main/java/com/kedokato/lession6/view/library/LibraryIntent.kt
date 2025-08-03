package com.kedokato.lession6.view.library

import com.kedokato.lession6.database.Entity.SongEntity

sealed class LibraryIntent {
    data object LoadSongs : LibraryIntent()
    object RequestPermissionAndLoadSongs : LibraryIntent()
    object PermissionGranted : LibraryIntent()
    data object RefreshSongs : LibraryIntent()
    data class AddSongToPlaylist(val playlistId: Long, val song: SongEntity) : LibraryIntent()
    data object ShowChoosePlaylistDialog : LibraryIntent()

    data class SongSelected(val song: SongEntity) : LibraryIntent()



}