package com.kedokato.lession6.view.playlist.component

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kedokato.lession6.R
import com.kedokato.lession6.model.Song
import com.kedokato.lession6.view.playlist.Menu
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