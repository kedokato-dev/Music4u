package com.kedokato.lession6.view.library

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.component.LoadingScreen
import com.kedokato.lession6.database.Entity.PlaylistWithSongs
import com.kedokato.lession6.database.Entity.SongEntity
import com.kedokato.lession6.model.Song
import com.kedokato.lession6.view.library.component.PlayListItem
import com.kedokato.lession6.view.playlist.myplaylist.MyPlaylistIntent
import com.kedokato.lession6.view.playlist.myplaylist.MyPlaylistViewModel
import com.kedokato.lession6.view.playlist.myplaylist.PlaylistItem
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LibraryScreen(modifier: Modifier) {

    val viewModel: LibraryViewModel = koinViewModel()
    val myPlaylistViewModel: MyPlaylistViewModel = koinViewModel()

    val state = viewModel.state.collectAsState().value

    val myPlaylistState = myPlaylistViewModel.state.collectAsState().value


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                viewModel.processIntent(LibraryIntent.PermissionGranted)
            }
        }
    )

    LaunchedEffect(state.requestedPermission) {
        state.requestedPermission?.let { permission ->
            permissionLauncher.launch(permission)
            viewModel.resetRequestedPermission()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.processIntent(LibraryIntent.RequestPermissionAndLoadSongs)
        myPlaylistViewModel.processIntent(MyPlaylistIntent.LoadPlaylists)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LibraryTopAppBar()
            PagerWithTabs(songs = state.songs, viewModel = viewModel, state = state)
        }

        if (state.isLoading) {
            LoadingScreen(
                message = "Loading Songs...",
                modifier = Modifier.matchParentSize()
            )
        }

        if (state.showChoosePlaylistDialog) {
            DialogChoosePlaylist(
                playlists = myPlaylistState.playlists,
                onDismiss = {
                    viewModel.processIntent(LibraryIntent.ShowChoosePlaylistDialog)
                },
                onAddToPlaylist = { playlistId ->
                    state.song?.let { viewModel.processIntent(LibraryIntent.AddSongToPlaylist(playlistId, it)) }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerWithTabs(songs: List<Song> = listOf(), viewModel: LibraryViewModel, state: LibraryState) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 } // Number of tabs
    )
    val scope = rememberCoroutineScope()
    val tabTitles = listOf("Local", "Remote")

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            contentColor = getCurrentColorScheme().primary,
            containerColor = getCurrentColorScheme().background,
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(title) },
                    selectedContentColor = getCurrentColorScheme().primary,
                    unselectedContentColor = getCurrentColorScheme().secondary

                )
            }
        }

        HorizontalPager(
            pageSize = PageSize.Fill,
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight()
                .background(getCurrentColorScheme().background)
        ) { page ->
            when (page) {
                0 -> LibraryContent(
                    songs = songs,
                    modifier = Modifier.fillMaxSize(),
                    onAddToPlaylist = {
                        viewModel.processIntent(LibraryIntent.ShowChoosePlaylistDialog)
                    },
                    viewModel = viewModel
                )

                1 -> Text(
                    text = "Remote Library",
                    color = getCurrentColorScheme().onBackground,
                    modifier = Modifier.fillMaxSize()
                )

                else -> Text(
                    text = "Unknown Page",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun LibraryContent(
    songs: List<Song>,
    modifier: Modifier = Modifier,
    onAddToPlaylist: () -> Unit,
    viewModel: LibraryViewModel
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item {

        }

        items(songs.size) { index ->
            PlayListItem(song = songs[index], modifier = Modifier.fillMaxSize(), onAddClick = {
                onAddToPlaylist()
                // convert song to SongEntity, thuong dung mapper nhung chua lam :v
                val songEntity = SongEntity(
                    songId = songs[index].id,
                    title = songs[index].name,
                    artist = songs[index].artist,
                    duration = parseDurationToMilliseconds(songs[index].duration),
                    albumArt = songs[index].image,
                    uri = songs[index].uri
                )
                viewModel.processIntent(LibraryIntent.SongSelected(songEntity))
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryTopAppBar() {
    val colorScheme = getCurrentColorScheme()
    TopAppBar(
        title = { Text(text = "Library", style = MaterialTheme.typography.headlineMedium) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorScheme.background,
            titleContentColor = colorScheme.onBackground,
            actionIconContentColor = colorScheme.onBackground,
            navigationIconContentColor = colorScheme.onBackground,
            scrolledContainerColor = colorScheme.background,
        ),
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}


@Composable
fun DialogChoosePlaylist(
    playlists: List<PlaylistWithSongs> = emptyList(),
    onDismiss: () -> Unit,
    onAddToPlaylist: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier.size(240.dp),
            colors = CardDefaults.cardColors(
                containerColor = getCurrentColorScheme().background,
                contentColor = getCurrentColorScheme().onBackground
            )
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
               item {
                   Text(text = "Choose Playlist")
               }

                items(playlists) { playlist ->
                    PlaylistItem(
                        playlist = playlist,
                        onClick = {
                            onAddToPlaylist(playlist.playlist.playlistId)
                            onDismiss()
                        },
                        onDeleteClick = { playlistId ->
                            onDismiss()
                        }
                    )
                }
            }
        }
    }

}

fun parseDurationToMilliseconds(durationString: String): Long {
    return try {
        val parts = durationString.split(":")

        when (parts.size) {
            2 -> {
                // Định dạng mm:ss
                val minutes = parts[0].toLongOrNull() ?: 0L
                val seconds = parts[1].toLongOrNull() ?: 0L
                (minutes * 60 + seconds) * 1000
            }
            3 -> {
                // Định dạng h:mm:ss
                val hours = parts[0].toLongOrNull() ?: 0L
                val minutes = parts[1].toLongOrNull() ?: 0L
                val seconds = parts[2].toLongOrNull() ?: 0L
                (hours * 3600 + minutes * 60 + seconds) * 1000
            }
            else -> {
                // Nếu định dạng không hợp lệ, trả về 0
                0L
            }
        }
    } catch (e: Exception) {
        // Nếu có lỗi parsing, trả về 0
        0L
    }
}



@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview() {
    LibraryScreen(modifier = Modifier)
}