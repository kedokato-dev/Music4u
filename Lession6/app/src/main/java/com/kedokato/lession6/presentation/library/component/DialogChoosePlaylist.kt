package com.kedokato.lession6.presentation.library.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.data.local.database.Entity.PlaylistEntity
import com.kedokato.lession6.data.local.database.Entity.PlaylistWithSongs
import com.kedokato.lession6.presentation.playlist.myplaylist.PlaylistItem




@Composable
fun DialogChoosePlaylist(
    playlists: List<PlaylistWithSongs> = emptyList(),
    onDismiss: () -> Unit,
    onAddToPlaylist: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier.width(240.dp)
                .height(400.dp),
            colors = CardDefaults.cardColors(
                containerColor = getCurrentColorScheme().background,
                contentColor = getCurrentColorScheme().onBackground
            )
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(text = "Choose Playlist",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        color = getCurrentColorScheme().onBackground,
                        textAlign = TextAlign.Center
                    )
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

@Preview
@Composable
private fun PreviewDialogChoosePlaylist() {
    DialogChoosePlaylist(
        playlists = listOf(
            PlaylistWithSongs(playlist = PlaylistEntity(1, "Playlist 1", 1), songs = emptyList()),
            PlaylistWithSongs(playlist = PlaylistEntity(1, "Playlist 1", 1), songs = emptyList())
        ),
        onDismiss = {},
        onAddToPlaylist = {}
    )
}