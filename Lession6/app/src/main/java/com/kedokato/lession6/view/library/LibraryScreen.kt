package com.kedokato.lession6.view.library

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImagePainter
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.component.LoadingScreen
import com.kedokato.lession6.model.Song
import com.kedokato.lession6.view.library.component.PlayListItem
import com.kedokato.lession6.view.playlist.PlaylistIntent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun LibraryScreen(modifier: Modifier) {

    val viewModel: LibraryViewModel = koinViewModel()

    val state = viewModel.state.collectAsState().value




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
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LibraryTopAppBar()
            PagerWithTabs(songs = state.songs)
        }

        if (state.isLoading) {
            LoadingScreen(
                message = "Loading Songs...",
                modifier = Modifier.matchParentSize()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerWithTabs(songs: List<Song> = listOf()) {
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
                    modifier = Modifier.fillMaxSize()
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
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(songs.size) { index ->
            PlayListItem(song = songs[index], modifier = Modifier.fillMaxSize())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryTopAppBar() {
    val colorScheme = getCurrentColorScheme()
    CenterAlignedTopAppBar(
        title = { Text(text = "Library", style = MaterialTheme.typography.headlineSmall) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorScheme.background,
            titleContentColor = colorScheme.onBackground,
            actionIconContentColor = colorScheme.onBackground,
            navigationIconContentColor = colorScheme.onBackground,
            scrolledContainerColor = colorScheme.background,
        ),
        // Thêm dòng này để TopAppBar không tự thêm padding
        windowInsets = WindowInsets(0, 0, 0, 0)
    )
}

val listSong: List<Song> = listOf(
    Song(
        id = 1,
        name = "Song 1",
        artist = "Artist 1",
        duration = "3:45",
        image = null,
        uri = null
    ),
    Song(
        id = 2,
        name = "Song 2",
        artist = "Artist 2",
        duration = "4:20",
        image = null,
        uri = null
    ),
    Song(
        id = 3,
        name = "Song 3",
        artist = "Artist 3",
        duration = "2:30",
        image = null,
        uri = null
    )
)


@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview() {
    LibraryScreen(modifier = Modifier)
}