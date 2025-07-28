package com.kedokato.lession6.navigation

import androidx.compose.runtime.saveable.Saver
import androidx.navigation3.runtime.NavKey
import com.kedokato.lession6.R
import kotlinx.serialization.Serializable


val bottomBarItems = listOf<BottomBarScreen>(
    BottomBarScreen.Home,
    BottomBarScreen.Playlist,
    BottomBarScreen.Library
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
    data object Playlist : BottomBarScreen(
        icon = R.drawable.play_list,
        title = "Search"
    )

    @Serializable
    data object Library : BottomBarScreen(
        icon = R.drawable.library,
        title = "Profile"
    )
}

val BottomBarScreenSaver = Saver<BottomBarScreen, String>(
    save = { it::class.simpleName ?: "Unknown" },
    restore = {
        when (it) {
            BottomBarScreen.Home::class.simpleName -> BottomBarScreen.Home
            BottomBarScreen.Playlist::class.simpleName -> BottomBarScreen.Playlist
            BottomBarScreen.Library::class.simpleName -> BottomBarScreen.Library
            else -> BottomBarScreen.Home
        }
    }
)