package com.kedokato.lession6.view.playlist

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
import com.kedokato.lession6.R
import com.kedokato.lession6.model.Song
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayListScreen(typeDisplay: Boolean, isSort: Boolean = false) {
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
fun PlayListItem(
    song: Song,
    isSort: Boolean,
    isDragging: Boolean = false,
    dragOffset: Offset = Offset.Zero,
    onDragStart: () -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDrag: (Offset) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .offset {
                if (isDragging) {
                    IntOffset(0, dragOffset.y.roundToInt())
                } else {
                    IntOffset.Zero
                }
            }
            .zIndex(if (isDragging) 1f else 0f)
            .background(if (isDragging) Color.DarkGray else Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = song.image),
            contentDescription = "Song Image",
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
        )
        Column {
            Text(
                text = song.name,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = song.artist,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        Text(
            text = song.duration,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.End
        )
        Image(
            painter = painterResource(id = R.drawable.dotx3),
            contentDescription = "More Options",
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
                .align(Alignment.CenterVertically)
                .clickable {
                    expanded = true
                },
            colorFilter = ColorFilter.tint(Color.White)
        )

        Menu(
            expanded = expanded,
            onDismiss = { expanded = false },
            song = song
        )

        if (isSort) {
            Image(
                painter = painterResource(id = R.drawable.drag),
                contentDescription = "Drag Handle",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { onDragStart() },
                            onDragEnd = { onDragEnd() },
                            onDrag = { change, dragAmount ->
                                onDrag(dragAmount)
                            }
                        )
                    }
            )
        }
    }
}

@Composable
fun PlayGridItem(song: Song) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box {
            Image(
                painter = painterResource(id = song.image),
                contentDescription = "Song Image",
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(14.dp)
                    .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(6.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dotx3),
                    contentDescription = "Dot x3 Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp)
                        .clickable { expanded = true },
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }

            Menu(
                expanded = expanded,
                onDismiss = { expanded = false },
                song = song
            )
        }

        Text(
            text = song.name,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = song.artist,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = song.duration,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
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

@Preview
@Composable
fun PlayListScreenPreview() {
    PlayListScreen(typeDisplay = true, isSort = true)
}

@Preview
@Composable
fun PlayListTopBarPreview() {
    PlayListTopBar(typeDisplay = false, onToggleDisplay = {}, isSort = true, onSort = {},
        onCancelSort = {})
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