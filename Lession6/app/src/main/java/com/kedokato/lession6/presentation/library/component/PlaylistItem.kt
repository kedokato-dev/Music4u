package com.kedokato.lession6.presentation.library.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.domain.model.Song
import org.koin.androidx.compose.get

@Composable
fun PlayListItem(
    song: Song,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit,
    onSongClick: (song: Song) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .background(getCurrentColorScheme().background)
            .fillMaxSize()
            .clickable { onSongClick(song) }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.image)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.DISABLED)
                .build(),
            contentDescription = song.name,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.nct),
            error = painterResource(id = R.drawable.apple_music),
            modifier = Modifier
                .size(64.dp)
                .padding(8.dp)
                .clip(RoundedCornerShape(2.dp))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp, end = 8.dp)
        ) {
            Text(
                text = song.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                color = getCurrentColorScheme().onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = song.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = getCurrentColorScheme().onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        Text(
            text = song.duration,
            style = MaterialTheme.typography.bodyMedium,
            color = getCurrentColorScheme().onBackground,
            modifier = Modifier
                .padding(8.dp)
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
            colorFilter = ColorFilter.tint(getCurrentColorScheme().onBackground)
        )

        Menu(
            expanded = expanded,
            onDismiss = { expanded = false },
            song = song,
            onAddClick = {
                onAddClick()
            }
        )
    }
}


@Composable
fun Menu(expanded: Boolean, onDismiss: () -> Unit, song: Song, onAddClick: () -> Unit = {}) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismiss() },
        modifier = Modifier
            .background(Color.DarkGray)
            .padding(8.dp),
    ) {
        DropdownMenuItem(
            text = { Text("Add to playlist", color = Color.White) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.add_to_playlist),
                    contentDescription = "Add to playlist icon",
                    tint = Color.White
                )
            },
            onClick = {
                onAddClick()
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