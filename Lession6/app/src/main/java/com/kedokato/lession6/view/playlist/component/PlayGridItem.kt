package com.kedokato.lession6.view.playlist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kedokato.lession6.R
import com.kedokato.lession6.model.Song
import com.kedokato.lession6.view.playlist.Menu


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