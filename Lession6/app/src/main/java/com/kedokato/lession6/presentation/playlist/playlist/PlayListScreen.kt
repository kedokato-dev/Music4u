package com.kedokato.lession6.presentation.playlist.playlist

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.domain.repository.SongLocalDataSource
import com.kedokato.lession6.presentation.playlist.component.PlayGridItem
import com.kedokato.lession6.presentation.playlist.component.PlayListItem
import com.kedokato.lession6.presentation.playlist.component.PlayListTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyPlaylistDetailScreen(
     playlistId: Long,
     playlistTitle: String,
) {
    val viewModel: PlaylistViewModel = koinViewModel()

    val state by viewModel.state.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.processIntent(PlaylistIntent.LoadSongs(playlistId))
    }

    PlaylistContent(
        tittle = playlistTitle,
        typeDisplay = state.displayType,
        isSort = state.isSorting,
        listSong = state.songs
    )


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlaylistContent(
    tittle: String = "My Playlist",
    typeDisplay: Boolean,
    isSort: Boolean = false,
    listSong: List<Song>
) {
    var draggedIndex by remember { mutableStateOf(-1) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    val listState = rememberLazyListState()

    val viewModel :PlaylistViewModel = koinViewModel()

    if (typeDisplay) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .background(getCurrentColorScheme().background)
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {
            item {
                PlayListTopBar(
                    tittle = "Playlist $tittle",
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
                key = { _, song -> song.id },
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
                    .background(getCurrentColorScheme().background)
                    .padding(top = 16.dp)
                    .padding(horizontal = 8.dp)
            ) {
                PlayListTopBar(
                    tittle = "Playlist $tittle",
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
                        .background(getCurrentColorScheme().background)
                ) {
                    items(listSong.size) { index ->
                        val song = listSong[index]
                        PlayGridItem(song)
                    }
                }
        }
    }
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

class FakeSongRepository : SongLocalDataSource {
    override suspend fun getAllSongs(): List<Song> {
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
    PlaylistContent(
        typeDisplay = true,
        isSort = false,
        listSong = dummySongs
    )
}