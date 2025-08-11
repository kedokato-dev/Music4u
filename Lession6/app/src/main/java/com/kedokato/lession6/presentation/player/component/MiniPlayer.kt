package com.kedokato.lession6.presentation.player.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.getCurrentColorScheme
import com.kedokato.lession6.R
import com.kedokato.lession6.domain.model.Song
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniPlayerMusic(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    progress: Float = 0.5f,
    song: Song? = null,
    onCloseMiniPlayer: () -> Unit = {},
    onPlayPauseMiniPlayer: () -> Unit = {},
    onClickMiniPlayer: () -> Unit = {},
    onSeek: (Float) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClickMiniPlayer)
            .background(getCurrentColorScheme().background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
        ) {
            // Background track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.5.dp)
                    .align(Alignment.Center)
                    .background(
                        color = getCurrentColorScheme().primary.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
            )

            // Active track
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(1.5.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        color = getCurrentColorScheme().primary,
                        shape = CircleShape
                    )
            )

            // Invisible slider for touch handling
            Slider(
                value = progress,
                onValueChange = { onSeek(it) },
                valueRange = 0f..1f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Transparent,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                ),
                thumb = {}
            )
        }

        // Nội dung mini player
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.size(32.dp),
                onClick = { onPlayPauseMiniPlayer() }
            ) {
                Icon(
                    painter = if (isPlaying) painterResource(R.drawable.pause) else painterResource(R.drawable.play),
                    contentDescription = "Play/Pause",
                    tint = getCurrentColorScheme().primary,
                )
            }

            Text(
                text = song?.name ?: "Unknown Song",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = getCurrentColorScheme().onBackground,
            )

            Text(
                text = song?.duration.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = getCurrentColorScheme().onBackground,
            )

            IconButton(
                onClick = onCloseMiniPlayer,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.cancel),
                    contentDescription = "Close",
                    tint = getCurrentColorScheme().primary
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderMini(
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit = {},
    value: Float = 0.7f,
){
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = 0f..1f,
        modifier = modifier.height(8.dp),
        colors = SliderDefaults.colors(
            thumbColor = Color.Transparent,
            activeTrackColor = getCurrentColorScheme().primary,
            inactiveTrackColor = getCurrentColorScheme().outline.copy(alpha = 0.3f)
        ),
        track = { sliderState ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            color = getCurrentColorScheme().outline.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(sliderState.value)
                        .height(2.dp)
                        .background(
                            color = getCurrentColorScheme().primary,
                            shape = CircleShape
                        )
                )
            }
        },
        thumb = { }
    )
}

val song = Song(
    id = 1,
    name = "Nhin em hanh phuc ben ai ?",
    artist = "Artist Name",
    duration = "3:40",
    image = null,
    uri = null,
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MiniPlayerMusicPreview() {
    MiniPlayerMusic(
        modifier = Modifier,
        isPlaying = true,
        song = song,
        onCloseMiniPlayer = {},
        onPlayPauseMiniPlayer = {},
        onClickMiniPlayer = {},
        onSeek = {}

    )
}