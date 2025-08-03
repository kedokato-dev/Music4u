package com.kedokato.lession6.view.playlist.myplaylist

import DialogAddPlaylist
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.database.Entity.PlaylistWithSongs
import com.kedokato.lession6.view.playlist.component.PlaylistEmptyView
import org.koin.androidx.compose.koinViewModel


@Composable
fun MyPlaylistScreen(
    onPlaylistClick: (playlistId: Long, title: String) -> Unit = { _, _ -> }
){

    val viewModel: MyPlaylistViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value


    if (state.playlists.isEmpty()){
        Text(text = "No playlist")
    }

    LaunchedEffect(Unit) {
        viewModel.processIntent(MyPlaylistIntent.LoadPlaylists)
    }

    val playlists = state.playlists.sortedBy { it.playlist.name }


    MyPlaylistContent(
        myPlaylist = playlists,
        onAddClick = {
            viewModel.processIntent(MyPlaylistIntent.OnAddPlaylistClick)
        },
        state = state,
        viewModel = viewModel,
        onPlaylistClick = onPlaylistClick
    )

}


@Composable
fun MyPlaylistContent(
    myPlaylist: List<PlaylistWithSongs> = emptyList(),
    onAddClick: () -> Unit = { },
    state: MyPlaylistState = MyPlaylistState(),
    viewModel: MyPlaylistViewModel = viewModel(),
    onPlaylistClick: (Long, String) -> Unit = { _, _ -> }
) {
    LazyColumn(
    ) {
        item {
            MyPlaylistTopBar(
                onAddClick = {
                    viewModel.processIntent(MyPlaylistIntent.OnAddPlaylistClick)
                }
            )
        }
        item {
            if(state.playlists.isEmpty()){
                PlaylistEmptyView (
                    onClick = { viewModel.processIntent(MyPlaylistIntent.OnAddPlaylistClick) }
                )
            }

            if (state.showDialog){
                DialogAddPlaylist(
                    onDismissRequest = {
                        viewModel.processIntent(MyPlaylistIntent.OnAddPlaylistClick)
                    },
                    onCreate = { title ->
                        viewModel.processIntent(MyPlaylistIntent.OnCreatePlaylist(title))
                    },
                    title = "Create Playlist",
                    subTitle = "Give your playlist a title",
                )
            }
        }
        items(myPlaylist.size) { index ->
            PlaylistItem(playlist = myPlaylist[index],
                onDeleteClick = { playlistId ->
                    viewModel.processIntent(MyPlaylistIntent.OnDeletePlaylist(playlistId))
                },
                onClick = { playlistId ->
                    onPlaylistClick(playlistId, myPlaylist[index].playlist.name)
                }
            )
        }
    }

}

@Composable
fun PlaylistItem(playlist: PlaylistWithSongs, onDeleteClick: (Long) -> Unit = {},
                 onClick: (Long) -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick(playlist.playlist.playlistId)
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.img_extra)
                .memoryCachePolicy(CachePolicy.DISABLED)
                .diskCachePolicy(CachePolicy.DISABLED)
                .build(),
            contentDescription = playlist.playlist.name,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.img_extra),
            error = painterResource(id = R.drawable.img4),
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(2.dp))
        )

        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
        ) {
            Text(
                text = playlist.playlist.name,
                style = MaterialTheme.typography.headlineSmall,
                color = getCurrentColorScheme().onBackground,
                modifier = Modifier.padding(bottom = 4.dp),
            )
            Text(
                text = "${playlist.songs.size} Song",
                style = MaterialTheme.typography.bodyMedium,
                color = getCurrentColorScheme().onBackground.copy(0.7f),
                modifier = Modifier.padding(bottom = 4.dp),
            )
        }

        Box {
            Icon(
                painter = painterResource(id = R.drawable.dotx3),
                contentDescription = "More Options",
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
                    .clickable { expanded = true },
                tint = getCurrentColorScheme().onBackground
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.remove),
                            contentDescription = "Delete Icon",
                            tint = getCurrentColorScheme().onBackground,
                            modifier = Modifier.size(24.dp),
                        )
                    },
                    text = { Text("Delete") },
                    onClick = {
                        expanded = false
                        onDeleteClick(playlist.playlist.playlistId)
                    }
                )
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "Edit Icon",
                            tint = getCurrentColorScheme().onBackground,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    text = { Text("Edit") },
                    onClick = {
                        expanded = false
                        /* Handle edit */
                    }
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPlaylistTopBar(
    onAddClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = "My Playlist", style = MaterialTheme.typography.headlineMedium) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = getCurrentColorScheme().background,
            titleContentColor = getCurrentColorScheme().onBackground,
            actionIconContentColor = getCurrentColorScheme().onBackground,
            navigationIconContentColor = getCurrentColorScheme().onBackground,
            scrolledContainerColor = getCurrentColorScheme().background,
        ),
        windowInsets = WindowInsets(0, 0, 0, 0),
        actions = {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = "Add Icon",
                    tint = getCurrentColorScheme().onBackground,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp)
                        .clickable { onAddClick() }
                )
        }

    )
}
