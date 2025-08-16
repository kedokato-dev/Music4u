package com.kedokato.music4u.navigation

import androidx.compose.runtime.saveable.Saver
import androidx.navigation3.runtime.NavKey
import com.kedokato.music4u.R
import kotlinx.serialization.Serializable


val bottomBarItems = listOf<BottomBarScreen>(
    BottomBarScreen.Home,
    BottomBarScreen.Library,
    BottomBarScreen.Playlist,
)

@Serializable
sealed class BottomBarScreen(
    val icon: Int,
    val title: String,
): NavKey {
    @Serializable
    data object Home : BottomBarScreen(
        icon = R.drawable.home,
        title = "Home"
    )

    @Serializable
    data object Library : BottomBarScreen(
        icon = R.drawable.libraryy,
        title = "Library"
    )

    @Serializable
    data object Playlist : BottomBarScreen(
        icon = R.drawable.play_list,
        title = "Playlist"
    )


}

val BottomBarScreenSaver = Saver<BottomBarScreen, String>(
    save = { it::class.simpleName ?: "Unknown" },
    restore = {
        when (it) {
            BottomBarScreen.Home::class.simpleName -> BottomBarScreen.Home
            BottomBarScreen.Library::class.simpleName -> BottomBarScreen.Library
            BottomBarScreen.Playlist::class.simpleName -> BottomBarScreen.Playlist
            else -> BottomBarScreen.Home
        }
    }
)