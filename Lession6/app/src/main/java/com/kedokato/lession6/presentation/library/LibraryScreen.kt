package com.kedokato.lession6.presentation.library

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
import androidx.core.net.toUri
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.data.local.database.Entity.PlaylistWithSongs
import com.kedokato.lession6.data.local.database.Entity.SongEntity
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.presentation.component.DisconnectInternet
import com.kedokato.lession6.presentation.component.LottieAnimationLoading
import com.kedokato.lession6.presentation.library.component.DialogChoosePlaylist
import com.kedokato.lession6.presentation.library.component.PlayListItem
import com.kedokato.lession6.presentation.playlist.myplaylist.MyPlaylistIntent
import com.kedokato.lession6.presentation.playlist.myplaylist.MyPlaylistViewModel
import com.kedokato.lession6.presentation.playlist.myplaylist.PlaylistItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LibraryScreen(modifier: Modifier,
                  onSongClick: (Song) -> Unit = {}) {

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
        myPlaylistViewModel.processIntent(MyPlaylistIntent.LoadPlaylists)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LibraryTopAppBar()
            PagerWithTabs(viewModel = viewModel, state = state, onSongClick = onSongClick)
        }

        if (state.isLoading) {
            LottieAnimationLoading(
                modifier = Modifier.fillMaxSize()
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
fun PagerWithTabs(viewModel: LibraryViewModel, state: LibraryState, onSongClick: (Song) -> Unit = {}) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )
    val scope = rememberCoroutineScope()
    val tabTitles = listOf("Local", "Remote")

    LaunchedEffect(pagerState.currentPage) {
        when (pagerState.currentPage) {
            0 -> {
                if (state.localSongs.isEmpty()) {
                    viewModel.processIntent(LibraryIntent.RequestPermissionAndLoadSongs)
                }
            }
            1 -> {
                if (state.remoteSongs.isEmpty()) {
                    viewModel.processIntent(LibraryIntent.LoadSongsFromRemote)
                }
            }
        }
    }

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
                    songs = state.localSongs,
                    modifier = Modifier.fillMaxSize(),
                    onAddToPlaylist = {
                        viewModel.processIntent(LibraryIntent.ShowChoosePlaylistDialog)
                    },
                    onSongClick = onSongClick,
                    viewModel = viewModel,
                    state = state
                )

                1 -> LibraryContent(
                    songs = state.remoteSongs,
                    modifier = Modifier.fillMaxSize(),
                    onAddToPlaylist = {
                        viewModel.processIntent(LibraryIntent.ShowChoosePlaylistDialog)
                    },
                    onSongClick = onSongClick,
                    viewModel = viewModel,
                    state = state
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
    onSongClick: (Song) -> Unit = {},
    viewModel: LibraryViewModel,
    state: LibraryState
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            if(state.errorLoadingFromRemote != null) {
                DisconnectInternet(
                    modifier = modifier,
                    onRetry = {
                        viewModel.processIntent(LibraryIntent.LoadSongsFromRemote)
                    }
                )
            }
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
                    uri = songs[index].uri?.toUri()
                )
                viewModel.processIntent(LibraryIntent.SongSelected(songEntity))
            },onSongClick = {
                onSongClick(songs[index])
            } )
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