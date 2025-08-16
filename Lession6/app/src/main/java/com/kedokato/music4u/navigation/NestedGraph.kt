package com.kedokato.music4u.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.data.service.MusicServiceController
import com.kedokato.music4u.domain.model.PlayerState
import com.kedokato.music4u.presentation.home.HomeScreen
import com.kedokato.music4u.presentation.home.top_albums.TopAlbumsDetailScreen
import com.kedokato.music4u.presentation.home.top_tracks.TopTracksDetailScreen
import com.kedokato.music4u.presentation.library.LibraryScreen
import com.kedokato.music4u.presentation.player.PlayerMusicScreen
import com.kedokato.music4u.presentation.player.component.MiniPlayerMusic
import com.kedokato.music4u.presentation.playlist.myplaylist.MyPlaylistScreen
import com.kedokato.music4u.presentation.playlist.playlist.MyPlaylistDetailScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestedGraph(onProfileClick: () -> Unit,
                musicServiceController: MusicServiceController) {
    val backStack = rememberNavBackStack<BottomBarScreen>(BottomBarScreen.Home)
    val colorScheme = getCurrentColorScheme()

    var currentBottomBarScreen: BottomBarScreen by rememberSaveable(
        stateSaver = BottomBarScreenSaver
    ) { mutableStateOf(BottomBarScreen.Home) }

    val stateHolder = rememberSaveableStateHolder()
    val playerState by musicServiceController.playerState.collectAsState(PlayerState())

    val currentScreen = backStack.lastOrNull()
    val shouldShowBottomBar = currentScreen is BottomBarScreen
    val shouldShowMiniPlayer = playerState.song != null &&
            (currentScreen is BottomBarScreen || currentScreen is RememberScreen.PlaylistDetailScreen)


    Scaffold(
        bottomBar = {
                Column {
                    if (shouldShowMiniPlayer) {
                        MiniPlayerMusic(
                            modifier = if(!shouldShowBottomBar){
                                Modifier.padding(bottom = 48.dp)
                            } else Modifier,
                            isPlaying = playerState.isPlaying,
                            progress = if (playerState.duration > 0) {
                                playerState.currentPosition.toFloat() / playerState.duration.toFloat()
                            } else 0f,
                            song = playerState.song,
                            onCloseMiniPlayer = {
                                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main)
                                    .launch {
                                        musicServiceController.stopSong()
                                    }
                            },
                            onPlayPauseMiniPlayer = {
                                if (playerState.isPlaying) {
                                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main)
                                        .launch {
                                            musicServiceController.pause()
                                        }
                                } else {
                                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main)
                                        .launch {
                                            musicServiceController.resume()
                                        }
                                }
                            },
                            onClickMiniPlayer = {
                                playerState.song?.let { song ->
                                    backStack.add(RememberScreen.PlayerMusicScreen(song))
                                }
                            },
                            onSeek = { progress ->
                                val position = (progress * playerState.duration).toLong()
                                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main)
                                    .launch {
                                        musicServiceController.seekTo(position)
                                    }
                            }
                        )
                    }

                    if (shouldShowBottomBar) {
                    NavigationBar(
                        containerColor = colorScheme.background,
                        modifier = Modifier.graphicsLayer {
                            clip = true
                        },
                        tonalElevation = 8.dp,
                    ) {
                        bottomBarItems.forEach { destination ->
                            NavigationBarItem(
                                selected = currentBottomBarScreen == destination,
                                icon = {
                                    Icon(
                                        painter = painterResource(destination.icon),
                                        contentDescription = "$destination icon",
                                        tint = if (currentBottomBarScreen == destination) colorScheme.primary else colorScheme.onBackground
                                    )
                                },
                                alwaysShowLabel = true,
                                label = { Text(destination.title) },
                                onClick = {
                                    if (backStack.lastOrNull() != destination) {
                                        if (backStack.lastOrNull() in bottomBarItems) {
                                            backStack.removeAt(backStack.lastIndex)
                                        }
                                        backStack.add(destination)
                                        currentBottomBarScreen = destination
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = colorScheme.onBackground,
                                    selectedTextColor = colorScheme.primary,
                                    indicatorColor = Color.Transparent,
                                    unselectedIconColor = colorScheme.onBackground,
                                    unselectedTextColor = colorScheme.onBackground,
                                ),
                            )
                        }
                    }
                }
                }

        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ){
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryDecorators = listOf(
                    rememberSavedStateNavEntryDecorator(),
                ),
                entryProvider = entryProvider {
                    entry<BottomBarScreen.Home> {
                        HomeScreen(
                            modifier = Modifier,
                            onSettingsClick = {
                            },
                            onProfileClick = onProfileClick,
                            onNavigationDetailTopAlbums = {
                                backStack.add(RememberScreen.DetailTopAlbumsScreen)
                            },
                            onNavigationDetailTopTracks = {
                                backStack.add(RememberScreen.DetailTopTracksScreen)
                            },
                        )
                    }

                    entry<BottomBarScreen.Library> {
                        LibraryScreen(
                            modifier = Modifier.fillMaxSize(),
                            onSongClick = { song ->
                                // Phát bài hát qua service thay vì navigate ngay
                                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                    musicServiceController.play(song)
                                }
                            }
                        )
                    }

                    entry<BottomBarScreen.Playlist> {
                        MyPlaylistScreen(
                            onPlaylistClick = { playlistId, playlistTitle ->
                                backStack.add(RememberScreen.PlaylistDetailScreen(playlistId, playlistTitle))
                            },
                        )
                    }

                    entry<RememberScreen.PlaylistDetailScreen> { entry ->
                        MyPlaylistDetailScreen(
                            playlistId = entry.playListId,
                            playlistTitle = entry.playlistTittle,
                            musicServiceController = musicServiceController,
                            onSongClick = { song ->
                                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                    musicServiceController.play(song)
                                }
                            }
                        )
                    }

                    entry<RememberScreen.PlayerMusicScreen> { entry ->
                        PlayerMusicScreen(
                            song = entry.song,
                            onNavigationIconClick = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }

                    entry<RememberScreen.DetailTopAlbumsScreen>{
                        TopAlbumsDetailScreen(
                            modifier = Modifier.fillMaxSize(),
                            onBackClick = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }

                    entry<RememberScreen.DetailTopTracksScreen>{
                        TopTracksDetailScreen(
                            modifier = Modifier.fillMaxSize(),
                            onNavigationIconClick = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }
                }
            )
        }
    }
}

