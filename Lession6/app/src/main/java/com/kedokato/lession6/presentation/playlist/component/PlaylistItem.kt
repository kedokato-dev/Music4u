package com.kedokato.lession6.presentation.playlist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.graphics.Color
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
import com.kedokato.lession6.R
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.presentation.playlist.playlist.Menu
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.image)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.DISABLED)
                .build(),
            contentDescription = song.name,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.img_extra),
            error = painterResource(id = R.drawable.img4),
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
                color = Color.White,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = song.artist,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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