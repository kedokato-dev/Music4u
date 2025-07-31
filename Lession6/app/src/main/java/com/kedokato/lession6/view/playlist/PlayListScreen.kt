package com.kedokato.lession6.view.playlist

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kedokato.lession6.R
import com.kedokato.lession6.model.Song
import com.kedokato.lession6.repository.PlaylistRepo
import com.kedokato.lession6.usecase.LoadSongsUseCase
import com.kedokato.lession6.view.playlist.component.PlayGridItem
import com.kedokato.lession6.view.playlist.component.PlayListItem

@Composable
fun MyPlayListScreen(
    context: Context = LocalContext.current
) {
    val viewModel: PlaylistViewModel = viewModel(
        factory = PlaylistViewModelFactory(context)
    )


    val state by viewModel.state.collectAsState()



    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                viewModel.processIntent(PlaylistIntent.LoadSongs)
            }
        }
    )

    LaunchedEffect(Unit) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.processIntent(PlaylistIntent.LoadSongs)
        } else {
            permissionLauncher.launch(permission)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.processIntent(PlaylistIntent.LoadSongs)
    }

    PlayListScreen(
        typeDisplay = state.displayType,
        isSort = state.isSorting,
        viewModel = viewModel,
        listSong = state.songs
    )


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayListScreen(
    typeDisplay: Boolean,
    isSort: Boolean = false,
    viewModel: PlaylistViewModel,
    listSong: List<Song>
) {
    var draggedIndex by remember { mutableStateOf(-1) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val listState = rememberLazyListState()

    if (typeDisplay) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(Color.Black)
                .padding(top = 16.dp)
                .padding(horizontal = 8.dp)
        ) {
            item {
                PlayListTopBar(
                    typeDisplay = typeDisplay,
                    onToggleDisplay = {
                        viewModel.processIntent(PlaylistIntent.DisplayType(typeDisplay))
                    },
                    isSort = isSort,
                    onSort = { /* Sort logic */ },
                    onCancelSort = { draggedIndex = -1 }
                )
            }

            itemsIndexed(
                items = listSong,
                key = { _, song -> song.id }
            ) { index, song ->
                val isDragging = draggedIndex == index
                PlayListItem(
                    song = song,
                    isSort = isSort,
                    isDragging = isDragging,
                    dragOffset = if (isDragging) dragOffset else Offset.Zero,
                    onDragStart = {

                    },
                    onDragEnd = {

                    },
                    onDrag = {
                    },
                    modifier = Modifier.animateItem()
                )
            }
        }
    } else {
            Column(
                modifier = Modifier
                    .background(Color.Black)
                    .padding(top = 16.dp)
                    .padding(horizontal = 8.dp)
            ) {
                PlayListTopBar(
                    typeDisplay = typeDisplay,
                    onToggleDisplay = {
                        viewModel.processIntent(PlaylistIntent.DisplayType(typeDisplay))
                    },
                    isSort = isSort,
                    onSort = { /* Sort logic */ },
                    onCancelSort = { draggedIndex = -1 }
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listSong.size) { index ->
                        val song = listSong[index]
                        PlayGridItem(song)
                    }
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayListTopBar(
    typeDisplay: Boolean,
    onToggleDisplay: () -> Unit,
    isSort: Boolean,
    onSort: () -> Unit,
    onCancelSort: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(text = "My Playlist", style = MaterialTheme.typography.headlineSmall) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White,
            scrolledContainerColor = Color.Black,
        ),
        navigationIcon = {
            if (isSort) {
                Icon(
                    painter = painterResource(R.drawable.cancel),
                    contentDescription = "Close Sort",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(24.dp)
                        .clickable { onCancelSort() }
                )
            }
        },
        actions = {
            if (isSort) {
                Icon(
                    painter = painterResource(R.drawable.tick),
                    contentDescription = "Sort Icon",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp)
                        .clickable { onSort() }
                )
            } else {
                Icon(
                    painter = painterResource(
                        if (typeDisplay) R.drawable.grid else R.drawable.list
                    ),
                    contentDescription = "Grid/List Icon",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp)
                        .clickable { onToggleDisplay() }
                )
                Icon(
                    painter = painterResource(R.drawable.sort),
                    contentDescription = "Sort Icon",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp)
                        .clickable { onSort() }
                )
            }
        },
    )
}

@Composable
fun Menu(expanded: Boolean, onDismiss: () -> Unit, song: Song) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismiss() },
        modifier = Modifier
            .background(Color.DarkGray)
            .padding(8.dp),
    ) {
        DropdownMenuItem(
            text = { Text("Remove from playlist", color = Color.White) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.remove),
                    contentDescription = "Delete Icon",
                    tint = Color.White
                )
            },
            onClick = {
//                onDismiss()
//                songs.remove(song)
            }
        )
        DropdownMenuItem(
            text = { Text("Share (coming soon)", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = "Share Icon",
                    tint = Color.White
                )
            },
            onClick = {
                onDismiss()
            }
        )
    }
}

class FakeSongRepository : PlaylistRepo {
    override suspend fun getSongsFromStorage(): List<Song> {
        TODO("Not yet implemented")
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun PlayListScreenPreview() {
    val dummySongs = List(10) { index ->
        Song(
            id = index.toLong(),
            name = "Chỉ còn ta và ta giữa trời đêm đầy sao $index",
            artist = "Chỉ còn ta và ta giữa trời đêm đầy sao",
            duration = "3:00",
            image = null,
            uri = null
        )
    }
    val fakeViewModel = PlaylistViewModel(LoadSongsUseCase(FakeSongRepository()))
    PlayListScreen(
        typeDisplay = true,
        isSort = false,
        viewModel = fakeViewModel,
        listSong = dummySongs
    )
}