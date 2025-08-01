package com.kedokato.lession6.view.library

sealed class LibraryIntent {
    data object LoadSongs : LibraryIntent()
    object RequestPermissionAndLoadSongs : LibraryIntent()
    object PermissionGranted : LibraryIntent()
}