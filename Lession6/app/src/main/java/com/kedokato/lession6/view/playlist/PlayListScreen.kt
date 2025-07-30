package com.kedokato.lession6.view.playlist

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kedokato.lession6.R
import com.kedokato.lession6.model.Song
import com.kedokato.lession6.view.playlist.component.PlayGridItem
import com.kedokato.lession6.view.playlist.component.PlayListItem
import com.kedokato.lession6.view.profile.ProfileViewModel
import kotlin.math.roundToInt




@Composable
fun MyPlayListScreen(
    viewModel: PlaylistViewModel = viewModel()
) {

    // Observe the state
    val state by viewModel.state.collectAsState()

    PlayListScreen(
        typeDisplay = state.displayType,
        isSort = state.isSorting,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayListScreen(typeDisplay: Boolean, isSort: Boolean = false, viewModel: PlaylistViewModel) {
    var songs by remember { mutableStateOf(listSong.toMutableList()) }
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
                    onToggleDisplay = { /* Toggle display logic */ },
                    isSort = isSort,
                    onSort = { /* Sort logic */ },
                    onCancelSort = { draggedIndex = -1 }
                )
            }

            itemsIndexed(
                items = songs,
                key = { _, song -> song.id }
            ) { index, song ->
                val isDragging = draggedIndex == index
                PlayListItem(
                    song = song,
                    isSort = isSort,
                    isDragging = isDragging,
                    dragOffset = if (isDragging) dragOffset else Offset.Zero,
                    onDragStart = {
                        if (isSort) {
                            draggedIndex = index
                        }
                    },
                    onDragEnd = {
                        draggedIndex = -1
                        dragOffset = Offset.Zero
                    },
                    onDrag = { offset ->
                        dragOffset += offset

                        // Calculate target index based on drag position
                        val itemHeight = 80.dp.value * 3 // Approximate item height in pixels
                        val targetIndex = (index + (dragOffset.y / itemHeight).roundToInt())
                            .coerceIn(0, songs.size - 1)

                        if (targetIndex != index && targetIndex in songs.indices) {
                            // Perform the reorder
                            val draggedItem = songs[index]
                            songs = songs.toMutableList().apply {
                                removeAt(index)
                                add(targetIndex, draggedItem)
                            }
                            draggedIndex = targetIndex
                            dragOffset = Offset.Zero
                        }
                    },
                    modifier = Modifier.animateItem()
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .background(Color.Black)
                .padding(top = 16.dp)
                .padding(horizontal = 8.dp)
        ) {
            items(listSong.size) { index ->
                val song = listSong[index]
                PlayGridItem(song)
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
                onDismiss()
                listSong.remove(song)
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

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun PlayListScreenPreview() {
    PlayListScreen(typeDisplay = false, isSort = true, viewModel = PlaylistViewModel())
}

val listSong = mutableStateListOf<Song>(
    Song(1, "Grainy days", "moody", "4:30", R.drawable.img1),
    Song(2, "Coffee", "Kainbeats", "3:45", R.drawable.img2),
    Song(3, "raindrops", "Rainyxxy", "2:50", R.drawable.img3),
    Song(4, "Tokyo", "SmYang", "5:15", R.drawable.img4),
    Song(5, "Lullaby", "iamfinenow", "3:20", R.drawable.img5),
    Song(6, "Midnight", "Kainbeats", "4:10", R.drawable.img1),
    Song(7, "Sunset", "moody", "3:55", R.drawable.img2),
    Song(8, "Dreamscape", "Rainyxxy", "4:05", R.drawable.img3),
    Song(9, "Whispers", "SmYang", "3:30", R.drawable.img4),
    Song(10, "Echoes", "iamfinenow", "4:25", R.drawable.img5),
    Song(11, "Shape of you", "moody", "4:30", R.drawable.img_extra),
    Song(12, "Coffee", "Kainbeats", "3:45", R.drawable.img1),
    Song(13, "raindrops", "Rainyxxy", "2:50", R.drawable.img2),
    Song(14, "Tokyo", "SmYang", "5:15", R.drawable.img3),
    Song(15, "Lullaby", "iamfinenow", "3:20", R.drawable.img4),
    Song(16, "Midnight", "Kainbeats", "4:10", R.drawable.img5),
    Song(17, "Sunset", "moody", "3:55", R.drawable.img2),
    Song(18, "Dreamscape", "Rainyxxy", "4:05", R.drawable.img1),
    Song(19, "Whispers", "SmYang", "3:30", R.drawable.img5),
    Song(20, "Echoes", "iamfinenow", "4:25", R.drawable.img_extra),
)