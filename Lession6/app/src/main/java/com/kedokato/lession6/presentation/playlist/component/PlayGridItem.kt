package com.kedokato.lession6.presentation.playlist.component

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.domain.model.PlayerState
import com.kedokato.lession6.domain.model.Song
import com.kedokato.lession6.presentation.component.LottieAnimationPlayingSong
import com.kedokato.lession6.presentation.playlist.playlist.Menu
import com.kedokato.lession6.presentation.playlist.playlist.PlaylistState


@Composable
fun PlayGridItem(song: Song,
                 modifier: Modifier = Modifier,
                 state: PlaylistState,
                 onSongClick: () -> Unit = {},
                 playerState: PlayerState
) {
    val isCurrentSongPlaying = playerState?.song?.id == song.id
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onSongClick() }
            .then(
                if (isCurrentSongPlaying) {
                    Modifier.background(getCurrentColorScheme().primary.copy(0.3f))
                } else {
                    Modifier
                }
            ),
    ) {
        var expanded by remember { mutableStateOf(false) }
        Box {
            Box(
                modifier = Modifier.size(150.dp)
            ) {
                AsyncImage(
                    model = song.image,
                    contentDescription = song.name,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.img_extra),
                    error = painterResource(id = R.drawable.img4),
                    modifier = Modifier
                        .size(150.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                if(isCurrentSongPlaying) {
                    LottieAnimationPlayingSong(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(100.dp)
                    )
                }
            }

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
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge,
            color = getCurrentColorScheme().onBackground,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = song.artist,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
            color = getCurrentColorScheme().onBackground,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = song.duration,
            style = MaterialTheme.typography.bodyMedium,
            color = getCurrentColorScheme().onBackground,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
    }
}