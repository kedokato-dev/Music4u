package com.kedokato.lession6

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/*
* Tran Anh Quan - Techtreck Session 3
* Lesson 9 - List Layout
* */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayListScreen(typeDisplay: Boolean) {
    if (typeDisplay) {
        LazyColumn(
            modifier = Modifier
                .background(Color.Black)
                .padding(top = 16.dp)
                .padding(horizontal = 8.dp)

        ) {
            items(listSong.size) { index ->
                val song = listSong[index]
                PlayListItem(song)
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
fun PlayListTopBar(typeDisplay: Boolean, onToggleDisplay: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(text = "My Playlist", style = MaterialTheme.typography.headlineSmall) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White,
            navigationIconContentColor = Color.White,
            scrolledContainerColor = Color.Black,
        ),
        actions = {
            if (typeDisplay) {
                Icon(
                    painter = painterResource(R.drawable.grid),
                    contentDescription = "Grid Icon",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp)
                        .clickable {
                            onToggleDisplay()
                        }
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.list),
                    contentDescription = "Grid Icon",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp)
                        .clickable {
                            onToggleDisplay()
                        }
                )
            }

            Icon(
                painter = painterResource(R.drawable.sort),
                contentDescription = "Search Icon",
                tint = Color.White,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(24.dp)
            )
        },
    )
}


@Composable
fun PlayListItem(song: Song) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth()
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
                    contentDescription = "Delete Icon",
                    tint = Color.White
                )
            },
            onClick = {
                onDismiss()
                // Handle share action here
            }
        )
    }
}


@Preview
@Composable
fun PlayListScreenPreview() {
    PlayListScreen(typeDisplay = false)
}

val listSong = mutableStateListOf<Song>(
    Song("Grainy days", "moody", "4:30", R.drawable.img1),
    Song("Coffee", "Kainbeats", "3:45", R.drawable.img2),
    Song("raindrops", "Rainyxxy", "2:50", R.drawable.img3),
    Song("Tokyo", "SmYang", "5:15", R.drawable.img4),
    Song("Lullaby", "iamfinenow", "3:20", R.drawable.img5),
    Song("Midnight", "Kainbeats", "4:10", R.drawable.img1),
    Song("Sunset", "moody", "3:55", R.drawable.img2),
    Song("Dreamscape", "Rainyxxy", "4:05", R.drawable.img3),
    Song("Whispers", "SmYang", "3:30", R.drawable.img4),
    Song("Echoes", "iamfinenow", "4:25", R.drawable.img5),
    Song("Shape of you", "moody", "4:30", R.drawable.img_extra),
    Song("Coffee", "Kainbeats", "3:45", R.drawable.img1),
    Song("raindrops", "Rainyxxy", "2:50", R.drawable.img2),
    Song("Tokyo", "SmYang", "5:15", R.drawable.img3),
    Song("Lullaby", "iamfinenow", "3:20", R.drawable.img4),
    Song("Midnight", "Kainbeats", "4:10", R.drawable.img5),
    Song("Sunset", "moody", "3:55", R.drawable.img2),
    Song("Dreamscape", "Rainyxxy", "4:05", R.drawable.img1),
    Song("Whispers", "SmYang", "3:30", R.drawable.img5),
    Song("Echoes", "iamfinenow", "4:25", R.drawable.img_extra),
)
