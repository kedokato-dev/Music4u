package com.kedokato.music4u.presentation.playlist.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.compose.getCurrentColorScheme
import com.kedokato.music4u.R
import com.kedokato.music4u.domain.model.PlayerState
import com.kedokato.music4u.domain.model.Song
import com.kedokato.music4u.presentation.component.LottieAnimationPlayingSong
import com.kedokato.music4u.presentation.playlist.playlist.Menu
import kotlin.math.roundToInt

@Composable
fun PlayListItem(
    song: Song,
    isSort: Boolean,
    isDragging: Boolean = false,
    dragOffset: Offset = Offset.Zero,
    onDragStart: () -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDrag: (Offset) -> Unit = {},
    modifier: Modifier = Modifier,
    onNavigationPlayerMusic: () -> Unit = {},
    playerState: PlayerState? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val isCurrentSongPlaying = playerState?.song?.id == song.id

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
            .then(
                if (isCurrentSongPlaying){
                    Modifier.background(getCurrentColorScheme().primary.copy(0.3f))
                } else {
                    Modifier.background(getCurrentColorScheme().background)
                }
            )
            .clickable(
                onClick = {
                    onNavigationPlayerMusic()
                }
            )
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.image)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.DISABLED)
                    .build(),
                contentDescription = song.name,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.img_extra),
                error = painterResource(id = R.drawable.apple_music),
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(2.dp))
            )

            if (isCurrentSongPlaying) {
                LottieAnimationPlayingSong(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            }
        }

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
                color = getCurrentColorScheme().onBackground,
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